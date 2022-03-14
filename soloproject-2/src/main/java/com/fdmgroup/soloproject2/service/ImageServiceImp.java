package com.fdmgroup.soloproject2.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.UserRepository;

@Service
public class ImageServiceImp implements ImageService{
	
	private UserRepository userRepository;
	private String imageDirectory = "src/main/webapp/images/";
	
	@Autowired
	public ImageServiceImp(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void saveAccountImage(MultipartFile image, User user) { 
		String oN = image.getOriginalFilename();
		if(oN.endsWith(".png") || oN.endsWith(".jpg") || oN.endsWith(".bmp") || oN.endsWith(".jpeg")) {
			String newFileName = user.getUserName() + oN.substring(oN.lastIndexOf("."));
			Path uploadPath = Paths.get(imageDirectory + "accountimages/" + newFileName);
			try {
				Files.copy(image.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
				user.setProfileImage(newFileName);
				userRepository.save(user);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteProfileImage(User user) { 
		Path path = Paths.get("src/main/webapp/images/accountimages/" + user.getProfileImage());
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String saveArticleImage(MultipartFile image, Project project, ArticleText article) throws IOException {
		if(!image.isEmpty()) {
			String oN = image.getOriginalFilename();
			if (oN.endsWith(".png") || oN.endsWith(".jpg") || oN.endsWith(".bmp") || oN.endsWith(".jpeg")) {
				String newFilename = article.getArticleId() + oN.substring(oN.lastIndexOf("."));
				Path uploadPath = Paths.get(imageDirectory + "/projects/" + project.getProjectName());
				if(!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				try (InputStream inputStream = image.getInputStream()){
					Path filePath = uploadPath.resolve(newFilename);
					Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					throw new IOException("Could not save file " + image.getOriginalFilename());
				}
				return newFilename;
			}
		}
		return null; 
	}
	
	

}
