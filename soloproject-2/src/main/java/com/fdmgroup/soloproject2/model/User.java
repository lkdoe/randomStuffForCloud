package com.fdmgroup.soloproject2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users",
		uniqueConstraints = @UniqueConstraint(columnNames = {"user_name", "email"}))
public class User {
	
	@Id
	@GeneratedValue
	private Integer userId;
	@Column(name = "user_name")
	private String userName;
//	@Size(min = 6, message = "Password must be at least 6 characters long. ")
	private String password;
	@Column(name = "email") 
//	@Email(message = "Email address must be of valid format. ")
	private String email;
	
	private String profileImage;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_role_id")
	private Role role;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_favourites",
				joinColumns = {@JoinColumn(name = "fk_user_id")},
				inverseJoinColumns = {@JoinColumn(name = "fk_project_id")})
	private List<Project> favourites;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_recieved_messages",
				joinColumns = {@JoinColumn(name = "fk_reciever_id")},
				inverseJoinColumns = {@JoinColumn(name = "fk_message_id")})
	private List<Message> messages;
	
	public User() {}
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.favourites = new ArrayList<>();
		this.messages = new ArrayList<>();
	}
	
	public String getEmail() {return email; }
	public Integer getUserId() { return userId; }
	public String getUserName() { return userName; }
	public Role getRoles() { return role; }
	public String getPassword() {return password;}
	public List<Project> getFavourites(){return favourites;}
	public String getProfileImage() {return profileImage;}
	public List<Message> getMessages(){return messages;}
	
	public void setUserId(Integer userId) { this.userId = userId; }
	public void setUserName(String userName) { this.userName = userName; }
	public void setEmail(String email) {this.email = email;}
	public void setRole(Role role) { this.role = role; }
	public void setPassword(String password) {this.password = password;}
	public void setFavourites(List<Project> projects) {this.favourites = projects;}
	public void setProfileImage(String imageName) {this.profileImage = imageName;}
	public void setMessages(List<Message> messages) {this.messages = messages;}
	
	public void addFavourite(Project project) {favourites.add(project); }
	public void addMessage(Message message) {messages.add(message);}
	
	public void removeFavourite(Project project) {favourites.remove(project);}
	public void removeMessage(Message message) {messages.remove(message);}
	
	@Override
	public String toString() { return "User [userName=" + userName + "]"; }
	
}
