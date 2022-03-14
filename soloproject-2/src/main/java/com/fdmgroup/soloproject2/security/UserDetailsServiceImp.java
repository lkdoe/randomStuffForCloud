package com.fdmgroup.soloproject2.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	private UserRepository userRepository;
	
	@Autowired
	public UserDetailsServiceImp(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optUser= userRepository.findByUserName(username);
		User user = optUser.orElseThrow(() -> new UsernameNotFoundException(username));
		return new UserPrincipal(user);
	}

}
