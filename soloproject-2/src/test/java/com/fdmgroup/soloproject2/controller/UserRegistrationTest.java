package com.fdmgroup.soloproject2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.security.SecurityConfig;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.RoleService;
import com.fdmgroup.soloproject2.service.UserService;

@SpringBootTest(classes = {ProfileController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc()
@ContextConfiguration(classes = {SecurityConfig.class})
class UserRegistrationTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	@MockBean
	private RoleService roleService;
	@MockBean
	private ProjectService projectService;
	
	@BeforeEach
	void setUp() throws Exception {}

	@Test
	public void goToLogin() throws Exception {
		mvc.perform(get("/login")).andExpect(view().name("loginView"));
	}
	
	@Test
	public void goToRegistration() throws Exception {
		mvc.perform(get("/registration")).andExpect(view().name("registeraccount"));
	}
	
	@Test
	public void registerNewAccount() throws Exception{
		when(userService.findByUserName("testname")).thenReturn(null);
		when(roleService.findByRoleName("USER")).thenReturn(null);
		mvc.perform(post("/register")
				.param("username", "testname").param("email", "test@mail.com")
				.param("password", "testpassword").param("repeatpassword", "testpassword"))
				.andExpect(flash().attribute("success", "You have been registered successfully and can now log in."))
				;
		verify(userService).registerUser(any(User.class));
	}
	
	@Test
	public void registerNewAccountWrongRepeatPassword() throws Exception{
		when(userService.findByUserName("testname")).thenReturn(null);
		when(roleService.findByRoleName("USER")).thenReturn(null);
		mvc.perform(post("/register")
				.param("username", "testname").param("email", "test@mail.com")
				.param("password", "testpassword").param("repeatpassword", "WRONGPASSWORD"))
				.andExpect(view().name("redirect:/registration"))
				.andExpect(flash().attribute("error", "The password must be repeatet correctly. "))
				;
		verify(userService, times(0)).registerUser(any(User.class));
	}
	
	@Test
	public void registerNewAccountUsernameAllreadyInUse() throws Exception{
		User testUser = new User("testname", "testpassword");
		when(userService.findByUserName("testname")).thenReturn(testUser);
		when(roleService.findByRoleName("USER")).thenReturn(null);
		mvc.perform(post("/register")
				.param("username", "testname").param("email", "test@mail.com")
				.param("password", "testpassword").param("repeatpassword", "testpassword"))
				.andExpect(flash().attribute("error", "Username already exists. Please choose a new Username. "))
				;
		verify(userService, times(0)).registerUser(any(User.class));
	}

	@Test
	@WithMockUser(username = "mockuser")
	public void addToFavouritesAddsProjectToFavourites() throws Exception {
		Project project = new Project();
		User mockuser = new User("mockuser", "password");
		when(projectService.findByProjectId(1)).thenReturn(project);
		when(userService.findByUserName("mockuser")).thenReturn(mockuser);
		mvc.perform(post("/addFavourite").param("projectid", "1"))
		;
		verify(userService).addToFavourites(project, mockuser);
	}
	
	@Test
	@WithMockUser(username = "mockuser")
	public void addToFavouritesRemovesProjectFromFavourites() throws Exception {
		Project project = new Project();
		User mockuser = new User("mockuser", "password");
		mockuser.addFavourite(project);
		when(projectService.findByProjectId(1)).thenReturn(project);
		when(userService.findByUserName("mockuser")).thenReturn(mockuser);
		mvc.perform(post("/addFavourite").param("projectid", "1"))
		;
		verify(userService).removeFromFavourites(project, mockuser);
	}
}
