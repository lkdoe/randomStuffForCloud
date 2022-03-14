package com.fdmgroup.soloproject2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan("com.fdmgroup.soloproject2")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
						.antMatchers("/css/**", "/**/*.css", "WEB-INF/jsp/**", "/**/*.png", "/**/*.jpg",
								"/register**", "/registr**", "/projects", "/groups", 
								"/h2/**", "/", "/search*").permitAll()
						.antMatchers("/management/**").hasRole("ADMIN")
						.anyRequest().authenticated()
						.and().formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll()
						.and().httpBasic()
						;
		
		httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().disable();
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
