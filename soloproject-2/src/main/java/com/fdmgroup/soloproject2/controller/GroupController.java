package com.fdmgroup.soloproject2.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.service.GroupService;
import com.fdmgroup.soloproject2.service.ProjectService;
import com.fdmgroup.soloproject2.service.UserService;

@Controller
public class GroupController {
	
	private GroupService groupService;
	private ProjectService projectService;
	private UserService userService;
	
	@Autowired
	public GroupController(GroupService groupService, ProjectService projectService, UserService userService) {
		this.userService = userService;
		this.groupService = groupService;
		this.projectService = projectService;
	}
	@GetMapping("/groups")
	public String groupOverview(ModelMap model, Principal principal) {
		if(principal != null && principal.getName() != null) {
			User user = userService.findByUserName(principal.getName());
			model.addAttribute(user);
		}
		model.addAttribute("groups", groupService.getAll());
		
		return "allgroups";
	}
	
	@GetMapping("/group/{groupName}")
	public String goToGroup(ModelMap model, @PathVariable String groupName, Principal principal) {
		HobbyGroup group = groupService.findByGroupName(groupName);
		if(group == null) {
			return "redirect:/groups";
		}
		User user = (principal != null && principal.getName() != null) ?
				userService.findByUserName(principal.getName()) : null;
		String membership;
		if(group.getNormalMembers().contains(user)) {
			membership = "member";
		} else if (group.getGroupMods().contains(user)) {
			membership = "moderator";
		} else if (group.getGroupApplicants().contains(user)) {
			membership = "applicant";
		} else {
			membership = "external";
		}
		model.addAttribute("user", user);
		model.addAttribute("membership", membership);
		model.addAttribute("group", group);
		return "/group";
	}
	
	@PostMapping("/creategroup")
	public String createGroup(ModelMap model, Principal principal,
					@RequestParam(name = "groupname")String groupName) {
		HobbyGroup group = new HobbyGroup(groupName);
		User user = userService.findByUserName(principal.getName());
		group.addModerator(user);
		groupService.registerGroup(group);
		model.addAttribute("user", user);
		return "redirect:/profile";
	}
	
	@PostMapping("/abdicate")
	public String abdicateAsMod(ModelMap model, Principal principal,
			@RequestParam(name = "groupid") Integer groupId) {
		HobbyGroup group = groupService.findByGroupID(groupId);
		User user = userService.findByUserName(principal.getName());
		groupService.abdicateAsMod(group, user);
		model.addAttribute("group", group);
		return "redirect:/group/" + group.getGroupName();
	}
	
	@PostMapping("/leavegroup")
	public String leaveGroup(Principal principal, @RequestParam(name = "groupid") Integer groupId) {
		HobbyGroup group = groupService.findByGroupID(groupId);
		User user = userService.findByUserName(principal.getName());
		String destination = groupService.leaveGroup(group, user);
		return "redirect:" + destination;
	}
	
	@PostMapping("/applyforgroup")
	public String applyForGroupMembership(Principal principal, 
											@RequestParam(name = "groupid") Integer groupId) {
		User user = userService.findByUserName(principal.getName());
		HobbyGroup group = groupService.findByGroupID(groupId);
		groupService.addApplicant(group, user);
		return "redirect:/group/" + group.getGroupName();
	}
	
	@PostMapping("/cancelapplication")
	public String cancelApplication(Principal principal,
									@RequestParam(name = "groupid") Integer groupId) {
		User user = userService.findByUserName(principal.getName());
		HobbyGroup group = groupService.findByGroupID(groupId);
		groupService.removeApplicant(group, user);
		return "redirect:/group/" + group.getGroupName();
	}
	
	@PostMapping("/acceptapplication")
	public String acceptApplication(@RequestParam(name = "username")String applicantName,
									@RequestParam(name = "groupid")Integer groupId,
									Principal principal) {
		User mod = userService.findByUserName(principal.getName());
		User applicant = userService.findByUserName(applicantName);
		HobbyGroup group = groupService.findByGroupID(groupId);
		groupService.acceptApplication(group, mod, applicant);
		return "redirect:/group/" + group.getGroupName();
	}

	@PostMapping("/denyapplication")
	public String denyApplication(@RequestParam(name = "username")String applicantName,
									@RequestParam(name = "groupid")Integer groupId,
									Principal principal) {
		User mod = userService.findByUserName(principal.getName());
		User applicant = userService.findByUserName(applicantName);
		HobbyGroup group = groupService.findByGroupID(groupId);
		groupService.denyApplication(group, mod, applicant);
		return "redirect:/group/" + group.getGroupName();
	}
	
	@PostMapping("/makemod")
	public String makeMod(@RequestParam(name = "userid") Integer userId,
							@RequestParam(name = "groupid")Integer groupId, 
							Principal principal) {
		User mod = userService.findByUserName(principal.getName());
		User member = userService.findByUserId(userId);
		HobbyGroup group = groupService.findByGroupID(groupId);
		groupService.promoteToMod(group, mod, member);
		return "redirect:/group/" + group.getGroupName();
	}
	
	@PostMapping("/throwout")
	public String throwOutOfGroup(@RequestParam(name = "userid") Integer userId,
							@RequestParam(name = "groupid")Integer groupId, 
							Principal principal) {
		User mod = userService.findByUserName(principal.getName());
		User member = userService.findByUserId(userId);
		HobbyGroup group = groupService.findByGroupID(groupId);
		groupService.throwOutOfGroup(group, mod, member);
		return "redirect:/group/" + group.getGroupName();
	}
}
