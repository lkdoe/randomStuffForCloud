package com.fdmgroup.soloproject2.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Message {

	@Id
	@GeneratedValue
	private Integer messageId;
	private LocalDateTime timeOfWriting;
	@ManyToOne
	@JoinColumn(name = "fk_sender_id")
	private User sender;
	private String title;
	@Column(columnDefinition = "TEXT")
	private String text;
	
	public Message() {}

	public Message(User sender, String title, String text) {
		this.sender = sender;
		this.title = title;
		this.text = text;
		this.timeOfWriting = LocalDateTime.now();
	}

	public Integer getMessageId() { return messageId; }
	public LocalDateTime getTimeOfWriting() { return timeOfWriting; }
	public User getSender() { return sender; }
	public String getTitle() { return title; }
	public String getText() { return text; }
	public void setMessageId(Integer messageId) { this.messageId = messageId; }
	public void setTimeOfWriting(LocalDateTime timeOfWriting) { this.timeOfWriting = timeOfWriting; }
	public void setSender(User sender) { this.sender = sender; }
	public void setTitle(String title) { this.title = title; }
	public void setText(String text) { this.text = text; }

	@Override
	public String toString() {
		return "Message [timeOfWriting=" + timeOfWriting + ", sender=" + sender + ", title=" + title + ", text=" + text
				+ "]";
	}

	@Override
	public int hashCode() { return Objects.hash(sender, text, timeOfWriting, title); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		Message other = (Message) obj;
		return Objects.equals(sender.getUserId(), other.sender.getUserId()) && Objects.equals(text, other.text)
				&& Objects.equals(timeOfWriting, other.timeOfWriting) && Objects.equals(title, other.title);
	}
	
	public Boolean contentEquals(Message otherM) {
		return Objects.equals(sender.getUserId(), otherM.getSender().getUserId())
				&& Objects.equals(title, otherM.getTitle())
				&& Objects.equals(text, otherM.getText());
	}
}
