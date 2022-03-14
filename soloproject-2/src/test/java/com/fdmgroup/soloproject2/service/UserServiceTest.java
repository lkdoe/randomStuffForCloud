package com.fdmgroup.soloproject2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.UserRepository;

@SpringBootTest(classes = {UserServiceImp.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@MockBean
	private UserRepository userRepository;
	
	@InjectMocks
	UserServiceImp userServiceImp;

	User testuser = new User();
	
	@BeforeEach
	void setUp() throws Exception {}

	@Test
	void testFindByUserName() {
		when(userRepository.findByUserName("test")).thenReturn(Optional.of(testuser));
		User foundUser = userServiceImp.findByUserName("test");
		assertEquals(testuser, foundUser);
		verify(userRepository).findByUserName("test");
	}

	@Test
	void testRegisterUser() { 
		userServiceImp.registerUser(testuser);
		verify(userRepository).save(testuser);
	}

}
