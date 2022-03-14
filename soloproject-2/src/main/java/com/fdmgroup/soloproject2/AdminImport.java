package com.fdmgroup.soloproject2;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fdmgroup.soloproject2.model.Role;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.service.RoleService;
import com.fdmgroup.soloproject2.service.UserService;

@Component
public class AdminImport implements InitializingBean{
	
	private PasswordEncoder encoder;
	private UserService userService;
	private RoleService roleService;
	
	@Autowired
	public AdminImport (PasswordEncoder encoder, 
			UserService userService,
			RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
		this.encoder = encoder;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(userService.findByUserName("testadmin") == null) {
			User testadmin = new User("testadmin", encoder.encode("password"));
			Role foundRole = roleService.findByRoleName("ADMIN");
			testadmin.setRole( (foundRole != null) ? foundRole : new Role("ADMIN") );
			userService.registerUser(testadmin);
		}
	}
	
	
}
