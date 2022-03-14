package com.fdmgroup.soloproject2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.ArticleRepository;
import com.fdmgroup.soloproject2.repository.GroupRepository;
import com.fdmgroup.soloproject2.repository.ProjectRepository;
import com.fdmgroup.soloproject2.repository.UserRepository;

@SpringBootTest(classes = {ProjectServiceImp.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
	
	@MockBean
	private ProjectRepository projectRepository;
	@MockBean
	private GroupRepository groupRepository;
	@MockBean
	private ArticleRepository articleRepository;
	@MockBean
	private UserRepository userRepository;
	
	@InjectMocks
	ImageServiceImp imageServiceImp;

	MockMultipartFile mockImage = 
			new MockMultipartFile("filename", "originalFilename.png", null, "content".getBytes());
	User user = new User("mockuser", "mockpass");
	Project project = new Project("testproject");
	ArticleText testArticle = new ArticleText("testArticleTitle");
	
	@BeforeEach
	void setUp() throws Exception {}
	
	@Test
	public void saveAccountImage() {
		imageServiceImp.saveAccountImage(mockImage, user);
		verify(userRepository).save(user);
	}
	
	@Test
	public void saveAccountImageFailsIfWrongFormat() {
		MockMultipartFile otherMockImage = 
				new MockMultipartFile("otherMockImage", "blah.txt", null, "content".getBytes());
		imageServiceImp.saveAccountImage(otherMockImage, user);
		verify(userRepository, times(0)).save(user);
	}
	
	@Test
	public void saveArticleImageWithCorrectFormat() throws IOException {
		testArticle.setArticleId(33);
		String expectedFileName = "33.png";
		String producedFileName = imageServiceImp.saveArticleImage(mockImage, project, testArticle);
		
		assertEquals(expectedFileName, producedFileName);
	}
	
}
