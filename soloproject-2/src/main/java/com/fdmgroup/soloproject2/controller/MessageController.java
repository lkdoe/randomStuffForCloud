package com.fdmgroup.soloproject2.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.soloproject2.model.Message;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.service.MessageService;
import com.fdmgroup.soloproject2.service.UserService;

@Controller
public class MessageController {

	private UserService userService;
	private MessageService messageService;
	
	@Autowired
	public MessageController(UserService userService, MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
	}
	
	@GetMapping("/messages")
	public String goToMessageOverview(ModelMap model, Principal principal) {
		if(principal != null && principal.getName() != null) {
			User user = userService.findByUserName(principal.getName());
			
			model.addAttribute("recipients", messageService.messagesSentTo(user));
			model.addAttribute("senders", messageService.messageRecievedFrom(user));
			model.addAttribute("user", user);
		}
		return "allmessages";
	}
	
	@GetMapping("/message/{recipientName}")
	public String writeMessageScreen(@PathVariable String recipientName, ModelMap model, Principal principal) {
		User sender = userService.findByUserName(principal.getName());
		User recipient = userService.findByUserName(recipientName);
		List<Message> messages = messageService.messagesBetween(sender, recipient);
		model.addAttribute("messages", messages);
		model.addAttribute("user", sender);
		model.addAttribute("recipient", recipient);
		return "writemessage";
	}
	
	@PostMapping("/writemessage")
	public String writeMessage(Principal principal, 
								@RequestParam(name = "recipientname")String recipientName,
								@RequestParam(name = "title")String messageTitle,
								@RequestParam(name = "messagebody")String messageBody) {
		User sender = userService.findByUserName(principal.getName());
		User recipient = userService.findByUserName(recipientName);
		messageService.writeMessage(sender, recipient, messageTitle, messageBody);
		
		return "redirect:/message/" + recipientName;
	}
}
