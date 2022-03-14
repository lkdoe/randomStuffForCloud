package com.fdmgroup.soloproject2.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.soloproject2.model.Article;
import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;

public interface ImageService {

	void saveAccountImage(MultipartFile image, User user);

	void deleteProfileImage(User user);

	/**
	 * @return The location of the saved file, 
	 * constructed from the name of the project and id of the article.
	 * @throws IOException 
	 */
	String saveArticleImage(MultipartFile image, Project project, ArticleText article) throws IOException;

}
