package com.fdmgroup.soloproject2.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.internal.compiler.codegen.IntegerCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.service.GroupService;
import com.fdmgroup.soloproject2.service.ImageService;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.UserService;

@Controller
public class ProjectController {

	private GroupService groupService;
	private ProjectService projectService;
	private UserService userService;
	private ImageService imageService;
	
	
	@Autowired
	public ProjectController(GroupService groupService, UserService userService, 
				ProjectService projectService, ImageService imageService) {
		this.groupService = groupService;
		this.userService = userService;
		this.projectService = projectService;
		this.imageService = imageService;
	}
	
	@GetMapping("/project/{projectname}")
	public String goToProjectView(@PathVariable String projectname, ModelMap model, Principal principal) {
		User user = userService.findByUserName(principal.getName());
		Project project = projectService.findByProjectName(projectname);
		Boolean isOwner = projectService.isProjectOwner(project, user);
		if(user.getFavourites().contains(project)) {
			model.addAttribute("heart", "/resources/icons/heartFull.png");
		} else {
			model.addAttribute("heart", "/resources/icons/heartEmpty.png");
		}
		model.addAttribute("project", project);
		model.addAttribute("user", user);
		model.addAttribute("group", groupService.findGroupOfProject(project));
		if(isOwner) {
			return "editproject";
		} else {
			return "projectview";
		}
	}
	
	@PostMapping("/createproject")
	public String createProject(RedirectAttributes redirectAttributes,
								@RequestParam(name = "groupid") Integer groupId, 
								@RequestParam(name = "projectname") String projectName) {
		Project project = new Project(projectName);
		HobbyGroup group = groupService.findByGroupID(groupId);
		if(projectService.findByProjectName(projectName) == null) {
			projectService.startProject(project, group);
			return "redirect:/project/" + projectName;
		} else {
			redirectAttributes.addFlashAttribute("ProjectCreationError", "Project Name already in use. "
					+ "Please choose a different name. ");
			return "redirect:/group/" + group.getGroupName();
		}
		
	}
	@PostMapping("/updateproject")
	public String updateProject(ModelMap model, 
				@RequestParam(name = "projectid") Integer projectId,
				@RequestParam(name = "projectname") String projectName,
				@RequestParam(name = "description") String projectDescription,
				@RequestParam(name = "projectTopic") String projectTopic) {
		Project project = projectService.updateProject(projectId, projectName, projectTopic, projectDescription);
		return "redirect:/project/" + project.getProjectName();
	}
	
	@PostMapping("/deleteproject")
	public String deleteArticle(Principal principal,
								@RequestParam(name = "projectid") Integer projectId) {
		User user = userService.findByUserName(principal.getName());
		String destination = projectService.deleteProjectById(projectId, user);
		return "redirect:" + destination;
	}
	
	
	@PostMapping("/writetextarticle")
	public String writeTextArticle(@RequestParam(name = "projectid") Integer projectId,
								@RequestParam(name = "title")String articleTitle,
								@RequestParam(name = "articletext")String articleText,
								@RequestParam(name = "image")MultipartFile image) throws IOException {
		Project project = projectService.findByProjectId(projectId);
		ArticleText article = projectService.writeTextArticle(projectId, articleTitle, articleText);
		String imageName = imageService.saveArticleImage(image, project, article);
		projectService.rememberArticleImage(article, imageName);
		return "redirect:/project/" + project.getProjectName();
	}
	
}
