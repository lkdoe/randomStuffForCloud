package com.fdmgroup.soloproject2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

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

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.security.SecurityConfig;
import com.fdmgroup.soloproject2.service.GroupService;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.UserService;

@SpringBootTest(classes = {GroupController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SecurityConfig.class})
class GroupControllerTest {

	@MockBean
	private GroupService groupService;
	@MockBean
	private ProjectService projectService;
	@MockBean
	private UserService userService;
	
	@Autowired
	private MockMvc mvc;
	
	User mockUser = new User("mockUser", "pass");
	HobbyGroup mockGroup = new HobbyGroup("mockGroup");
	
	@BeforeEach
	void setUp() throws Exception {
		when(userService.findByUserName("mockUser")).thenReturn(mockUser);
		when(groupService.findByGroupID(1)).thenReturn(mockGroup);
	}

	@Test
	public void testSeeAllGroupsWithoutLogin() throws Exception { 
		List<HobbyGroup> allGroups = new ArrayList<>();
		when(groupService.getAll()).thenReturn(allGroups);
		mvc.perform(get("/groups"))
			.andExpect(model().attribute("groups", allGroups))
			.andExpect(view().name("allgroups"))
		;
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void seeAllGroupsWithLogin() throws Exception {
		List<HobbyGroup> allGroups = new ArrayList<>();
		when(groupService.getAll()).thenReturn(allGroups);
		when(userService.findByUserName("mockUser")).thenReturn(mockUser);
		mvc.perform(get("/groups"))
			.andExpect(model().attribute("groups", allGroups))
			.andExpect(model().attribute("user", mockUser))
			.andExpect(view().name("allgroups"))
		;
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void groupViewWithUserNotInGroupAndGroupNotExisting() throws Exception {
		when(groupService.findByGroupName("mockGroup")).thenReturn(null);
		mvc.perform(get("/group/mockGroup"))
			.andExpect(view().name("redirect:/groups"));
	}

	@Test
	@WithMockUser(username = "mockUser")
	public void groupViewWithUserNotInGroup() throws Exception {
		when(groupService.findByGroupName("mockGroup")).thenReturn(mockGroup);
		mvc.perform(get("/group/mockGroup"))
			.andExpect(view().name("/group"))
			.andExpect(model().attribute("user", mockUser))
			.andExpect(model().attribute("membership", "external"))
			.andExpect(model().attribute("group", mockGroup))
			;
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void createNewGroup() throws Exception {
		mvc.perform(post("/creategroup").param("groupname", "newMockGroup"))
			.andExpect(view().name("redirect:/profile"));
		verify(groupService).registerGroup(any(HobbyGroup.class));
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void abdicateAsGroupModerator() throws Exception {
		mockGroup.addModerator(mockUser);
		mvc.perform(post("/abdicate").param("groupid", "1"))
			.andExpect(view().name("redirect:/group/mockGroup"))
		;
		verify(groupService).abdicateAsMod(mockGroup, mockUser);
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void testLeaveGroup() throws Exception {
		when(groupService.leaveGroup(mockGroup, mockUser)).thenReturn("/group/mockGroup");
		mvc.perform(post("/leavegroup").param("groupid", "1"))
			.andExpect(view().name("redirect:/group/mockGroup"))
		;
		verify(groupService).leaveGroup(mockGroup, mockUser);
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void testApplyForGroupMembership() throws Exception {
		mvc.perform(post("/applyforgroup").param("groupid", "1"));
		verify(groupService).addApplicant(mockGroup, mockUser);
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void testCancelApplication() throws Exception {
		mvc.perform(post("/cancelapplication").param("groupid", "1"));
		verify(groupService).removeApplicant(mockGroup, mockUser);
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void testAcceptApplication() throws Exception {
		User mockApplicant = new User("mockApplicant", "mockpass");
		when(userService.findByUserName("mockApplicant")).thenReturn(mockApplicant);
		mvc.perform(post("/acceptapplication")
				.param("username", "mockApplicant").param("groupid", "1"));
		verify(groupService).acceptApplication(mockGroup, mockUser, mockApplicant);
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void testDenyApplication() throws Exception {
		User mockApplicant = new User("mockApplicant", "mockpass");
		when(userService.findByUserName("mockApplicant")).thenReturn(mockApplicant);
		mvc.perform(post("/denyapplication")
				.param("username", "mockApplicant").param("groupid", "1"));
		verify(groupService).denyApplication(mockGroup, mockUser, mockApplicant);
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void testMakeMod() throws Exception {
		User mockApplicant = new User("mockApplicant", "mockpass");
		when(userService.findByUserId(22)).thenReturn(mockApplicant);
		mvc.perform(post("/makemod")
				.param("userid", "22").param("groupid", "1"));
		verify(groupService).promoteToMod(mockGroup, mockUser, mockApplicant);
	}
	
	@Test
	@WithMockUser(username = "mockUser")
	public void testThrowMemberOutOfGroup() throws Exception {
		User mockApplicant = new User("mockApplicant", "mockpass");
		when(userService.findByUserId(22)).thenReturn(mockApplicant);
		mvc.perform(post("/throwout")
				.param("userid", "22").param("groupid", "1"));
		verify(groupService).throwOutOfGroup(mockGroup, mockUser, mockApplicant);
	}
}
