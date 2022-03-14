package com.fdmgroup.soloproject2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdmgroup.soloproject2.model.Article;
import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.ArticleRepository;
import com.fdmgroup.soloproject2.repository.GroupRepository;
import com.fdmgroup.soloproject2.repository.ProjectRepository;
import com.fdmgroup.soloproject2.repository.UserRepository;

@SpringBootTest(classes = {ProjectServiceImp.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

	@MockBean
	private ProjectRepository projectRepository;
	@MockBean
	private GroupRepository groupRepository;
	@MockBean
	private ArticleRepository articleRepository;
	@MockBean
	private UserRepository userRepository;
	
	@InjectMocks
	ProjectServiceImp projectService;
	
	List<Project> testProjects = new ArrayList<>();
	ArticleText textArticle = new ArticleText();
	User user = new User("name", null);
	Project project = new Project("testproject");
	HobbyGroup group = new HobbyGroup("testgroup");
	
	@BeforeEach
	void setUp() throws Exception {}

	@Test
	void testNewest() {
		when(projectRepository.findNewestProjects()).thenReturn(testProjects);
		List<Project> foundList = projectService.newest();
		assertEquals(testProjects, foundList);
		verify(projectRepository).findNewestProjects();
	}

	@Test
	public void testRememberArticleImage() {
		String imageName = "imageName";
		projectService.rememberArticleImage(textArticle, imageName);
		verify(articleRepository).save(textArticle);
	}
	
	@Test
	public void getRecommendedAndSearch() {
		String exampleTopic = "exampleTopic";
		project.setProjectTopic(exampleTopic);
		List<Project> searchResults = List.of(project);
		user.addFavourite(project);
		when(projectRepository.findByProjectDescriptionContaining(exampleTopic))
			.thenReturn(searchResults);
		when(projectRepository.findByProjectNameContaining(exampleTopic))
			.thenReturn(searchResults);
		when(projectRepository.findByProjectTopicContaining(exampleTopic))
			.thenReturn(searchResults);
		Collection<Project> expected = new HashSet<>();
		expected.add(project);
		Collection<Project> recom = projectService.recommended(user);
		assertEquals(expected, recom);
	}
	
	@Test
	public void testDeleteProjectById() {
		group.setProjects(new ArrayList<>());
		group.addProject(project);
		group.addModerator(user);
		when(projectRepository.findById(1)).thenReturn(Optional.of(project));
		when(groupRepository.findByProjects(project)).thenReturn(group);
		when(userRepository.findByFavourites(project)).thenReturn(List.of(user));
		
		String redirection = projectService.deleteProjectById(1, user);
		
		assertEquals("/", redirection);
	}
	
	@Test
	public void writeANewArticle() {
		ArticleText article = new ArticleText("testtitle");
		project.setArticles(new ArrayList<>());
		article.setArticleId(1);
		article.setArticleText("testtext");
		project.addArticle(article);
		when(projectRepository.findById(1)).thenReturn(Optional.of(project));
		when(articleRepository.save(any())).thenReturn(article);
		when(articleRepository.findById(1)).thenReturn(Optional.of(article));
		
		ArticleText writtenArticle = projectService.writeTextArticle(1, "testtitle", "testtext");
		assertEquals(article, writtenArticle);
	}
}
