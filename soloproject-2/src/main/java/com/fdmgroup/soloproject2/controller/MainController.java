package com.fdmgroup.soloproject2.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.UserService;

@Controller
public class MainController {
	
	private UserService userService;
	private ProjectService projectService;
	
	@Autowired
	public MainController(UserService userService, ProjectService projectService) {
		this.userService = userService;
		this.projectService = projectService;
	}

	@GetMapping("/")
	public String goHome(ModelMap model, Principal principal) {
		if(principal != null && principal.getName() != null) {
			User user = userService.findByUserName(principal.getName());
			Collection<Project> favourites = projectService.recommended(user);
			Set<Project> recommended = new HashSet<>();
			recommended.addAll(favourites);
			recommended.addAll(projectService.newest());
			model.addAttribute("recommended", recommended);
			model.addAttribute("user", user);
		} else {
			List<Project> newest = projectService.newest();
			model.addAttribute("recommended", newest);
			model.addAttribute("user", null);
		}
		return "index";
	}
	
	@GetMapping("/search")
	public String searchProjects(ModelMap model, Principal principal, 
			@RequestParam(name = "searchterm") String searchterm) {
		Set<Project> recommended = new HashSet<>();
		recommended.addAll(projectService.search(searchterm));
		model.addAttribute("wasSearch", true);
		model.addAttribute("recommended", recommended);
		if(principal != null && principal.getName() != null) {
			User user = userService.findByUserName(principal.getName());
			model.addAttribute("user", user);
		} else {
			model.addAttribute("user", null);
		}
		return "index";
	}
	
	@GetMapping("/topics")
	public String seeAllTopics(ModelMap model, Principal principal) {
		if(principal != null && principal.getName() != null) {
			User user = userService.findByUserName(principal.getName());
			model.addAttribute(user);
		}
		Set<String> topics = new HashSet<>();
		topics.addAll(projectService.getAllTopics());
		model.addAttribute("topics", topics);
		return "alltopics";
	}
	
	@GetMapping("/projects")
	public String seeAllProjects(ModelMap model, Principal principal) {
		if(principal != null && principal.getName() != null) {
			User user = userService.findByUserName(principal.getName());
			model.addAttribute("recommended", projectService.recommended(user));
			model.addAttribute("user", user);
		} else {
			List<Project> newest = projectService.newest();
			model.addAttribute("recommended", newest);
			model.addAttribute("user", null);
		}
		return "index";
	}
}
