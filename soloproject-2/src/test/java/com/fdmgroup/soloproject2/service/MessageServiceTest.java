package com.fdmgroup.soloproject2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fdmgroup.soloproject2.model.Message;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.MessageRepository;
import com.fdmgroup.soloproject2.repository.UserRepository;

@SpringBootTest(classes = {MessageServiceImp.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

	@MockBean
	MessageRepository messageRepository;
	@MockBean
	UserRepository userRepository;
	
	@InjectMocks
	MessageServiceImp messageService;
	
	User someUser = new User("so", "sp");
	User otherUser = new User("oo", "op");
	User thirdUser = new User("to", "tp");
	
	@BeforeEach
	void setUp() throws Exception {
		someUser.setUserId(1);
		otherUser.setUserId(2);
		thirdUser.setUserId(3);
	}

	@Test
	void testMessagesBetween() { 
		Message firstMessage = new Message(otherUser, "title1", "text1");
		firstMessage.setTimeOfWriting(LocalDateTime.of(2022, 02, 12, 02, 43));
		Message secondMessage = new Message(thirdUser, "title2", "text2");
		secondMessage.setTimeOfWriting(LocalDateTime.of(2022, 02, 12, 03, 00));
		Message thirdMessage = new Message(someUser, "title3", "text3");
		thirdMessage.setTimeOfWriting(LocalDateTime.of(2022, 03, 01, 19, 00));
		// Sender
		someUser.addMessage(firstMessage);
		someUser.addMessage(secondMessage);
		// Reciever
		otherUser.addMessage(thirdMessage);
		
		List<Message> expectedMessages = new ArrayList<>();
		expectedMessages.addAll(List.of(thirdMessage, firstMessage));

		List<Message> foundMessages = messageService.messagesBetween(someUser, otherUser);
		
		assertEquals(expectedMessages, foundMessages);
	}

	@Test
	public void testWriteMessage() {
		Message mockMessage = new Message(someUser, "mockTitle", "mockText");
		messageService.writeMessage(someUser, otherUser, "mockTitle", "mockText");
		
		verify(userRepository).save(otherUser);
		assertTrue(otherUser.getMessages().get(0).contentEquals(mockMessage));
	}

	@Test
	public void testMessageRecievedFrom() {
		Message firstMessage = new Message(otherUser, "title1", "text1");
		firstMessage.setTimeOfWriting(LocalDateTime.of(2022, 02, 12, 02, 43));
		Message secondMessage = new Message(thirdUser, "title2", "text2");
		secondMessage.setTimeOfWriting(LocalDateTime.of(2022, 02, 12, 03, 00));
		someUser.addMessage(firstMessage);
		someUser.addMessage(secondMessage);
		Collection<User> expectedUsers = new HashSet<>();
		expectedUsers.addAll(List.of(thirdUser, otherUser));
		
		Collection<User> foundUsers = messageService.messageRecievedFrom(someUser);
		
		assertEquals(expectedUsers, foundUsers);
	}

	@Test
	public void testMessagesSentTo() {
		messageService.messagesSentTo(someUser);
		verify(userRepository).findByMessagesSenderOrderByMessagesTimeOfWritingDesc(someUser);
	}

}
