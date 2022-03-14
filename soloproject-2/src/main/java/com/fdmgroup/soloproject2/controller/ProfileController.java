package com.fdmgroup.soloproject2.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.Role;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.service.GroupService;
import com.fdmgroup.soloproject2.service.ImageService;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.RoleService;
import com.fdmgroup.soloproject2.service.UserService;

@Validated
@Controller
public class ProfileController {

	private UserService userService;
	private RoleService roleService;
	private ImageService imageService;
	private ProjectService projectService;
	private GroupService groupService;
	private PasswordEncoder encoder;
	
	@Autowired
	public ProfileController(UserService userService, RoleService roleService, GroupService groupService,  
			ImageService imageService, ProjectService projectService, PasswordEncoder encoder) {
		this.userService = userService;
		this.roleService = roleService;
		this.imageService = imageService;
		this.projectService = projectService;
		this.groupService = groupService;
		this.encoder = encoder;
	}
	
	
	@GetMapping("/login")
	public String goToLogin() {
		return "loginView";
	}
	
	@GetMapping("/registration")
	public String goToRegistration() {
		return "registeraccount";
	}
	
	@GetMapping("/profile")
	public String goToProfile(ModelMap model, Principal principal) {
		User user = (principal != null && principal.getName() != null) ? 
				userService.findByUserName(principal.getName()) : null;
		List<HobbyGroup> myGroups = groupService.getMyGroups(user); 
		model.addAttribute("mygroups", myGroups);
		model.addAttribute(user);
		return "profilepage";
	}
	
	@PostMapping("/register")
	public String registerUser(ModelMap model,
					RedirectAttributes redirectAttributes,
					@Pattern(regexp = "^[a-zA-Z_0-9]*$", 
								message="Please use only letters, digits and underscore. ")
					@RequestParam(name = "username") String userName,
					@RequestParam(name = "email") 
					@Email(message = "Email must be of valid format. ") String email,
					@RequestParam(name = "password") 
					@Size(min = 6, message = "Password must be at least 6 characters long. ") 
					String password,
					@RequestParam(name = "repeatpassword") String repeatPassword) {
		if(!password.equals(repeatPassword)) {
			redirectAttributes.addFlashAttribute("error", "The password must be repeatet correctly. ");
			return "redirect:/registration";
		}
		if(userService.findByUserName(userName) != null ) {
			redirectAttributes.addFlashAttribute("error", "Username already exists. Please choose a new Username. ");
			return "redirect:/registration";
		}
		Role existingRole = roleService.findByRoleName("USER");
		Role role = (existingRole == null) ? new Role("USER") : existingRole;
		User user = new User(userName, encoder.encode(password));
		user.setRole(role);
		user.setEmail(email);
		userService.registerUser(user);
		String successMessage = "You have been registered successfully and can now log in.";
		redirectAttributes.addFlashAttribute("success", successMessage);
		return "redirect:/login";
	}
	
	@PostMapping("/profileimage")
	public String replaceProfileImage(Principal principal, ModelMap model, 
						@RequestParam(name = "image")MultipartFile image) {
//		String oN = image.getOriginalFilename();
		User user = userService.findByUserName(principal.getName());
		imageService.saveAccountImage(image, user);
		model.addAttribute("user", user);
		return "redirect:/profile";
	}
	
	@PostMapping("/deleteaccountimage")
	public String deleteProfileImage(Principal principal, ModelMap model) {
		User user = userService.findByUserName(principal.getName());
		imageService.deleteProfileImage(user);
		model.addAttribute("user", user);
		return "redirect:/profile";
	}
	
	@PostMapping("/addFavourite")
	public String addToFavourites(Principal principal, 
			@RequestParam(name = "projectid")Integer projectId) {
		Project project = projectService.findByProjectId(projectId);
		User user = userService.findByUserName(principal.getName());
		if(user.getFavourites().contains(project)) {
			userService.removeFromFavourites(project, user);
		}else {
			userService.addToFavourites(project, user);
		}
		return "redirect:/project/" + project.getProjectName();
	}

}
