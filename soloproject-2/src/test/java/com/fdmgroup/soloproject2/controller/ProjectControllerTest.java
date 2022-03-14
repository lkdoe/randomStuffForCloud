package com.fdmgroup.soloproject2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.fdmgroup.soloproject2.model.Article;
import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.security.SecurityConfig;
import com.fdmgroup.soloproject2.service.GroupService;
import com.fdmgroup.soloproject2.service.ImageService;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {ProjectController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SecurityConfig.class})
class ProjectControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	ProjectService projectService;
	@MockBean
	ImageService imageService;
	@MockBean
	GroupService groupService;
	@MockBean
	UserService userService;
	
	final String mockUserName = "mockUser";
	User mockUser = new User(mockUserName, "pass");
	String mockProjectName = "someproject";
	Project mockProject = new Project(mockProjectName);
	HobbyGroup mockGroup = new HobbyGroup("someGroup");
	
	@BeforeEach
	void setUp() throws Exception {
		when(groupService.findByGroupID(1)).thenReturn(mockGroup);
		when(userService.findByUserName(mockUserName)).thenReturn(mockUser);
		when(projectService.findByProjectName(mockProjectName)).thenReturn(mockProject);
		when(groupService.findGroupOfProject(mockProject)).thenReturn(mockGroup);
		when(projectService.isProjectOwner(mockProject, mockUser)).thenReturn(true);
		
	}

	@Test
	@WithMockUser(username = mockUserName)
	void testGoToProjectViewWhenUserIsOwner() throws Exception {
		mvc.perform(get("/project/" + mockProjectName)).andExpect(ResultMatcher.matchAll(
				model().attribute("project", mockProject),
				model().attribute("user", mockUser),
				model().attribute("group", mockGroup),
				model().attribute("heart", "/resources/icons/heartEmpty.png"),
				view().name("editproject")
				));
	}
	@Test
	@WithMockUser(username = mockUserName)
	void testGoToProjectViewWhenUserIsNotOwnerAndLikesProject() throws Exception {
		HobbyGroup mockGroup = new HobbyGroup("someGroup");
		mockUser.addFavourite(mockProject);
		when(groupService.findGroupOfProject(mockProject)).thenReturn(mockGroup);
		when(projectService.isProjectOwner(mockProject, mockUser)).thenReturn(false);
		mvc.perform(get("/project/" + mockProjectName)).andExpect(ResultMatcher.matchAll(
				model().attribute("project", mockProject),
				model().attribute("user", mockUser),
				model().attribute("group", mockGroup),
				model().attribute("heart", "/resources/icons/heartFull.png"),
				view().name("projectview")
				));
	}

	@Test
	@WithMockUser(username = mockUserName)
	void testCreateProjectWhoseNameDoesNotYetExist() throws Exception {
		when(projectService.findByProjectName(mockProjectName))
			.thenReturn(null);
		mvc.perform(post("/createproject")
						.param("groupid", "1")
						.param("projectname", mockProjectName))
			.andExpect(view().name("redirect:/project/" + mockProjectName))
		;
		verify(projectService).startProject(any(Project.class), any(HobbyGroup.class));
	}

	@Test
	@WithMockUser(username = mockUserName)
	void testCreateProjectWhoseNameAlreadyExists() throws Exception {
		when(projectService.findByProjectName(mockProjectName))
			.thenReturn(mockProject);
		mvc.perform(post("/createproject")
						.param("groupid", "1")
						.param("projectname", mockProjectName))
			.andExpect(view().name("redirect:/group/" + mockGroup.getGroupName()))
		;
		verify(projectService, times(0)).startProject(any(), any());
	}

	@Test
	@WithMockUser(username = mockUserName)
	void testUpdateProject() throws Exception { 
		Project updated = new Project("name");
		when(projectService.updateProject(1, "name", "sometopic", "somedescription"))
			.thenReturn(updated);
		mvc.perform(post("/updateproject")
				.param("projectid", "1")
				.param("projectname", "name")
				.param("description", "somedescription")
				.param("projectTopic", "sometopic"))
			.andExpect(view().name("redirect:/project/name"));
	}
	
	@Test
	@WithMockUser(username = mockUserName)
	void testDeleteProject() throws Exception { 
		when(projectService.deleteProjectById(1, mockUser)).thenReturn("/there");
		mvc.perform(post("/deleteproject").param("projectid", "1"))
				.andExpect(view().name("/there"))
		;
		verify(projectService).deleteProjectById(1, mockUser);
	}
	

	@Test
	@WithMockUser
	void testWriteTextArticle() throws Exception { 
		Project project = new Project();
		ArticleText article = new ArticleText();
		when(projectService.findByProjectId(1)).thenReturn(project);
		when(projectService.writeTextArticle(1, "articletitle", "test text")).thenReturn(article);
		when(imageService.saveArticleImage(any(), any(), any())).thenReturn("imagename");
		mvc.perform(multipart("/writetextarticle").file("image", "testimage".getBytes())
							.param("projectid", "1")
							.param("title", "articletitle")
							.param("articletext", "test text"))
			.andExpect(status().is3xxRedirection())
		;
		verify(projectService).writeTextArticle(1, "articletitle", "test text");
		verify(projectService).rememberArticleImage(article, "imagename");
	}

}
