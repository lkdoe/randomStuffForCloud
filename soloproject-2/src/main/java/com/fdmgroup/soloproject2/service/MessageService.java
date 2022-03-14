package com.fdmgroup.soloproject2.service;

import java.util.Collection;
import java.util.List;

import com.fdmgroup.soloproject2.model.Message;
import com.fdmgroup.soloproject2.model.User;

public interface MessageService {

	List<Message> messagesBetween(User sender, User recipient);

	void writeMessage(User sender, User recipient, String messageTitle, String messageBody);

	Collection<User> messageRecievedFrom(User user);

	Collection<User> messagesSentTo(User user);

}
