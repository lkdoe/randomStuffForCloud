package com.fdmgroup.soloproject2.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.soloproject2.model.Message;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.MessageRepository;
import com.fdmgroup.soloproject2.repository.UserRepository;

@Service
public class MessageServiceImp implements MessageService {

	private MessageRepository messageRepository;
	private UserRepository userRepository;
	
	@Autowired
	public MessageServiceImp(MessageRepository messageRepository, UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Message> messagesBetween(User sender, User recipient) { 
		List<Message> messages = new ArrayList<>();
		for(Message m : sender.getMessages()) {
			if(m.getSender().getUserId() == recipient.getUserId()) {
				messages.add(m);
			}
		}
		for(Message m : recipient.getMessages()) {
			if(m.getSender().getUserId() == sender.getUserId()) {
				messages.add(m);
			}
		}
		messages.sort((m1, m2) -> -m1.getTimeOfWriting().compareTo(m2.getTimeOfWriting()));
		return messages; 
	}

	@Override
	public void writeMessage(User sender, User recipient, String messageTitle, String messageBody) {
		Message message = new Message(sender, messageTitle, messageBody);
		recipient.addMessage(message);
//		messageRepository.save(message);
		userRepository.save(recipient);
	}

	@Override
	public Collection<User> messageRecievedFrom(User user) {
		Set<User> exchangeUsers = new HashSet<>();
		List<Message> messages = user.getMessages();
		messages.sort((m1, m2) -> m1.getTimeOfWriting().compareTo(m2.getTimeOfWriting()));
		for(Message m : messages) {
			exchangeUsers.add(m.getSender());
		}
		return exchangeUsers; 
	}

	@Override
	public Collection<User> messagesSentTo(User user) {
		Set<User> recipients = new HashSet<>();
		recipients.addAll(userRepository.findByMessagesSenderOrderByMessagesTimeOfWritingDesc(user));
		return recipients;
	}
	
	
}
