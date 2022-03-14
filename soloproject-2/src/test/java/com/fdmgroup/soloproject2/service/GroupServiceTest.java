package com.fdmgroup.soloproject2.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.GroupRepository;
import com.fdmgroup.soloproject2.repository.UserRepository;

@SpringBootTest(classes = {GroupServiceImp.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@MockBean
	GroupRepository groupRepository;
	@MockBean
	UserRepository userRepository;
	
	@InjectMocks
	GroupServiceImp groupService;
	
	HobbyGroup group = new HobbyGroup("mockGroup");
	User mockUser = new User("mockUser", "");
	Project mockProject = new Project("mockProject");
	
	@BeforeEach
	void setUp() throws Exception {}
	
	@Test
	void testFindByGroupName() { 
		groupService.findByGroupName("mockGroup");
		verify(groupRepository).findByGroupName("mockGroup");
	}

	@Test
	void testGetAll() {
		groupService.getAll();
		verify(groupRepository).findAll();
	}

	@Test
	void testGetMyGroups() {
		List<HobbyGroup> mockGroupList = new ArrayList<>();
		mockGroupList.add(group);
		groupService.getMyGroups(mockUser);
		verify(groupRepository).findByGroupMods(mockUser);
		verify(groupRepository).findByNormalMembers(mockUser);
	}

	@Test
	void testRegisterGroup() { 
		groupService.registerGroup(group);
		verify(groupRepository).save(group);
	}

	@Test
	void testFindByGroupID() { 
		HobbyGroup foundGroup = groupService.findByGroupID(33);
		verify(groupRepository).findById(33);
	}

	@Test
	void testAbdicateAsModFailsWhenNoOtherUsers() { 
		group.addModerator(mockUser);
		groupService.abdicateAsMod(group, mockUser);
		verify(groupRepository).save(group);
		assertTrue(group.getGroupMods().contains(mockUser));
	}

	@Test
	void testAbdicateAsModSucceedsWhenOtherMods() { 
		group.addModerator(mockUser);
		group.addModerator(new User("anotherMod", ""));
		groupService.abdicateAsMod(group, mockUser);
		verify(groupRepository).save(group);
		assertTrue(!group.getGroupMods().contains(mockUser));
	}
	
	@Test
	void testAbdicateAsModFailsWhenUserNotModerator() { 
		group.addMember(mockUser);
		groupService.abdicateAsMod(group, mockUser);
		assertTrue(!group.getGroupMods().contains(mockUser) 
				&& group.getNormalMembers().contains(mockUser));
	}

	@Test
	void testLeaveGroupDoesNotDeleteGroupWhenOtherMembers() { 
		group.addMember(mockUser);
		group.addModerator(new User("mockMod", "null"));
		groupService.leaveGroup(group, mockUser);
		verify(groupRepository).save(group);
		verify(groupRepository, times(0)).delete(group);
	}
	
	@Test
	void testLeaveGroupDeletesGroupWhenNoMembersOrMods() { 
		group.addMember(mockUser);
		group.setProjects(new ArrayList<>());
		groupService.leaveGroup(group, mockUser);
		verify(groupRepository).save(group);
		verify(groupRepository).delete(group);
	}
	
	@Test
	void testLeaveGroupRemovesProjectsFromFavourites() { 
		group.addMember(mockUser);
		group.setProjects(new ArrayList<>());
		Project project = new Project("someproject");
		User anotherUser = new User("another", "pass");
		anotherUser.addFavourite(project);
		group.addProject(project);
		when(userRepository.findByFavourites(project)).thenReturn(List.of(anotherUser));
		groupService.leaveGroup(group, mockUser);
		verify(groupRepository).save(group);
		verify(groupRepository).delete(group);
	}

	@Test
	void testFindGroupOfProject() {
		HobbyGroup foundGroup = groupService.findGroupOfProject(mockProject);
		verify(groupRepository).findByProjects(mockProject);
	}

	@Test
	void testAddApplicant() {
		groupService.addApplicant(group, mockUser);
		verify(groupRepository).save(group);
		assertTrue(group.getGroupApplicants().contains(mockUser));
	}

	@Test
	void testRemoveApplicant() {
		group.addApplicant(mockUser);
		groupService.removeApplicant(group, mockUser);
		assertFalse(group.getGroupApplicants().contains(mockUser));
		verify(groupRepository).save(group);
	}

	@Test
	void testAcceptApplication() {
		User mockMod = new User("mockMod", "mp");
		group.addModerator(mockMod);
		group.addApplicant(mockUser);
		
		groupService.acceptApplication(group, mockMod, mockUser);
		
		verify(groupRepository).save(group);
		assertTrue(!group.getGroupApplicants().contains(mockUser)
				&& group.getNormalMembers().contains(mockUser));
	}

	@Test
	void testAcceptApplicationFailsIfOperatorNotModerator() {
		User mockMod = new User("mockMod", "mp");
		group.addApplicant(mockUser);		
		groupService.acceptApplication(group, mockUser, mockMod);
		
		verify(groupRepository, times(0)).save(group);
		assertTrue(group.getGroupApplicants().contains(mockUser)
				&& !group.getNormalMembers().contains(mockUser));
	}
	
	@Test
	void testDenyApplication() { 
		User mockMod = new User("mockMod", "mp");
		group.addModerator(mockMod);
		group.addApplicant(mockUser);
		
		groupService.denyApplication(group, mockMod, mockUser);
		
		verify(groupRepository).save(group);
		assertTrue(!group.getGroupApplicants().contains(mockUser)
				&& !group.getNormalMembers().contains(mockUser));
	}
	@Test
	void testDenyApplicationFailsWhenOperatorNotModerator() { 
		User mockMod = new User("mockMod", "mp");
		group.addApplicant(mockUser);
		
		groupService.denyApplication(group, mockMod, mockUser);
		
		verify(groupRepository, times(0)).save(group);
		assertTrue(group.getGroupApplicants().contains(mockUser)
				&& !group.getNormalMembers().contains(mockUser));
	}

	@Test
	void testPromoteToMod() {
		User mockMod = new User("mockMod", "mp");
		group.addModerator(mockMod);
		group.addMember(mockUser);
		
		groupService.promoteToMod(group, mockMod, mockUser);
		
		verify(groupRepository).save(group);
		assertTrue(!group.getNormalMembers().contains(mockUser)
				&& group.getGroupMods().contains(mockUser));
	}
	
	@Test
	void testPromoteToModFailsWhenOperatorNotModerator() {
		User mockMod = new User("mockMod", "mp");
		group.addMember(mockUser);
		
		groupService.promoteToMod(group, mockMod, mockUser);
		
		verify(groupRepository, times(0)).save(group);
		assertTrue(group.getNormalMembers().contains(mockUser)
				&& !group.getGroupMods().contains(mockUser));
	}

	@Test
	void testThrowOutOfGroup() { 
		User mockMod = new User("mockMod", "mp");
		group.addModerator(mockMod);
		group.addMember(mockUser);
		
		groupService.throwOutOfGroup(group, mockMod, mockUser);
		
		verify(groupRepository).save(group);
		assertTrue(!group.getNormalMembers().contains(mockUser));
	}
	
	@Test
	void testThrowOutOfGroupFailsWhenOperatorNotModerator() {
		User mockMod = new User("mockMod", "mp");
		group.addMember(mockUser);
		
		groupService.throwOutOfGroup(group, mockMod, mockUser);
		
		verify(groupRepository, times(0)).save(group);
		assertTrue(group.getNormalMembers().contains(mockUser));
	}

}
