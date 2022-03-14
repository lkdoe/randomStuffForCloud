package com.fdmgroup.soloproject2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.internal.Lists;
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
import org.springframework.test.web.servlet.ResultMatcher;

import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.security.SecurityConfig;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.UserService;

@SpringBootTest(classes = {MainController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SecurityConfig.class})
class MainControllerTest {
	@Autowired
	private MockMvc mvc;
	@MockBean
	private UserService userService;
	@MockBean
	private ProjectService projectService;
	
	final String mockUserName = "mockUser";
	User mockUser = new User();
	List<Project> projects = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		when(userService.findByUserName(mockUserName)).thenReturn(mockUser);
	}

	@Test
	public void testGoHomeWithoutUser() throws Exception {
		mvc.perform(get("/"))
				.andExpect(view().name("index"))
				.andExpect(model().attribute("recommended", projects))
		;
	}
	
	@Test
	@WithMockUser(username = mockUserName)
	public void testGoHomeWithUser() throws Exception {
		projects.add(new Project("something"));
		List<Project> newest = List.of(new Project("anotherthing"));
		when(projectService.newest()).thenReturn(newest);
		when(projectService.recommended(mockUser)).thenReturn(projects);
		Set<Project> returnSet = new HashSet<>();
		returnSet.addAll(projects);
		returnSet.addAll(newest);
		mvc.perform(get("/"))
			.andExpect(view().name("index"))
			.andExpect(model().attribute("recommended", returnSet))
		;
	}
	
	@Test
	public void testTheSearchFunction() throws Exception {
		Collection<Project> searchResult = new ArrayList<Project>();
		searchResult.add(new Project("p"));
		Set<Project> expected = new HashSet<>();
		expected.addAll(searchResult);
		when(projectService.search("SOMETHING")).thenReturn(searchResult);
		mvc.perform(get("/search").param("searchterm", "SOMETHING"))
			.andExpect(ResultMatcher.matchAll(
					model().attribute("wasSearch", true),
					model().attribute("recommended", expected),
//					model().attribute("user", null),
					view().name("index")
					));
		verify(projectService).search("SOMETHING");
	}
	@Test
	@WithMockUser(username = mockUserName)
	public void testTheSearchFunctionWithUser() throws Exception {
		Collection<Project> searchResult = new ArrayList<Project>();
		searchResult.add(new Project("p"));
		Set<Project> expected = new HashSet<>();
		expected.addAll(searchResult);
		when(projectService.search("SOMETHING")).thenReturn(searchResult);
		mvc.perform(get("/search").param("searchterm", "SOMETHING"))
			.andExpect(ResultMatcher.matchAll(
					model().attribute("wasSearch", true),
					model().attribute("recommended", expected),
					model().attribute("user", mockUser),
					view().name("index")
					));
		verify(projectService).search("SOMETHING");
	}
	
	
	@Test
	@WithMockUser(username = mockUserName)
	public void testGoToAllTopicsPageWithUser() throws Exception {
		Collection<String> allTopics = new ArrayList<>();
		allTopics.addAll(List.of("a", "b", "c"));
		Set<String> expected = new HashSet<>(allTopics);
		
		when(projectService.getAllTopics()).thenReturn(allTopics);
		
		mvc.perform(get("/topics")).andExpect(ResultMatcher.matchAll(
				view().name("alltopics"),
				model().attribute("topics", expected),
				model().attribute("user", mockUser)
				));
	}
	
	@Test
	public void testSeeAllProjects() throws Exception {
		when(projectService.newest()).thenReturn(projects);
		mvc.perform(get("/projects"))
			.andExpect(model().attribute("recommended", projects))
			.andExpect(view().name("index"))
			;
	}
	
	@Test
	@WithMockUser(username = mockUserName)
	public void testSeeAllProjectsForUser() throws Exception {
		when(projectService.recommended(mockUser)).thenReturn(projects);
		mvc.perform(get("/projects"))
			.andExpect(model().attribute("recommended", projects))
			.andExpect(model().attribute("user", mockUser))
			.andExpect(view().name("index"))
			;
	}
}
