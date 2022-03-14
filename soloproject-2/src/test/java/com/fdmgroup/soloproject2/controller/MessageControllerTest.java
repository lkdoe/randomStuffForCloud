package com.fdmgroup.soloproject2.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fdmgroup.soloproject2.model.Message;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.security.SecurityConfig;
import com.fdmgroup.soloproject2.service.MessageService;
import com.fdmgroup.soloproject2.service.UserService;

@SpringBootTest(classes = {MessageController.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SecurityConfig.class})
class MessageControllerTest {

	@Autowired
	MockMvc mvc;
	@MockBean
	private UserService userService;
	@MockBean
	private MessageService messageService;
		
	final String mockUserName = "mockUser";
	User mockUser = new User();
	User user1 = new User("user1", "pass");
	User user2 =new User("user2", "pass");
	Collection<User> senders = new ArrayList<>(List.of(user1));
	Collection<User> recipients = new ArrayList<>(List.of(user2));
	@BeforeEach
	void setUp() throws Exception {
		when(userService.findByUserName(mockUserName)).thenReturn(mockUser);
		when(userService.findByUserName("user1")).thenReturn(user1);
	}


	@Test
	@WithMockUser(username = mockUserName)
	void testGoToMessageOverview() throws Exception {
		when(messageService.messagesSentTo(mockUser)).thenReturn(recipients);
		when(messageService.messageRecievedFrom(mockUser)).thenReturn(senders);
		mvc.perform(get("/messages")).andExpect(ResultMatcher.matchAll(
				model().attribute("recipients", recipients),
				model().attribute("senders", senders),
				view().name("allmessages")
				));
	}

	@Test
	@WithMockUser(username = mockUserName)
	void testWriteMessageScreen() throws Exception {
		List<Message> messages = new ArrayList<>();
		when(messageService.messagesBetween(mockUser, user1))
				.thenReturn(messages);
		mvc.perform(get("/message/user1")).andExpect(ResultMatcher.matchAll(
				model().attribute("messages", messages),
				model().attribute("user", mockUser),
				model().attribute("recipient", user1),
				view().name("writemessage")
				));
	}

	@Test
	@WithMockUser(username = mockUserName)
	void testWriteMessage() throws Exception {
		mvc.perform(post("/writemessage").param("recipientname", "user1")
				.param("title", "sometitle").param("messagebody", "sometext"))
				.andExpect(view().name("redirect:/message/user1"))
				;
		verify(messageService).writeMessage(mockUser, user1, "sometitle", "sometext");
	}

}
