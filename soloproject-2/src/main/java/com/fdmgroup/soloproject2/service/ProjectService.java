package com.fdmgroup.soloproject2.service;

import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.soloproject2.model.Article;
import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;

public interface ProjectService {

	Collection<Project> recommended(User user);

	List<Project> newest();

	void startProject(Project project, HobbyGroup group);

	Project findByProjectName(String projectname);

	Boolean isProjectOwner(Project project, User user);

	Project findByProjectId(Integer projectId);

	Project updateProject(Integer projectId, String projectName, String projectTopic, String projectDescription);

	ArticleText writeTextArticle(Integer projectId, String articleTitle, String articleText);
	
	void saveArticle(Article article);

	void rememberArticleImage(ArticleText article, String imageName);

	Collection<Project> search(String searchterm);

	Collection<String> getAllTopics();

	String deleteProjectById(Integer projectId, User user);


}
