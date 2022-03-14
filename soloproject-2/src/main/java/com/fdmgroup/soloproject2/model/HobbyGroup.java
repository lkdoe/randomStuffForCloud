package com.fdmgroup.soloproject2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"group_name"}))
public class HobbyGroup {
	@Id 
	@GeneratedValue
	private Integer groupId;
	@Column(name = "group_name")
	private String groupName;
	private String groupDescription;
//	@ElementCollection
//	private List<String> images;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Project> projects;
	
	@ManyToMany
	@JoinTable(name = "group_members", 
						joinColumns = @JoinColumn(name = "fk_group_id"),
						inverseJoinColumns = @JoinColumn(name = "fk_user_id")
				)
	private List<User> normalMembers;
	
	@ManyToMany
	@JoinTable(name = "group_moderators", 
						joinColumns = @JoinColumn(name = "fk_group_id"),
						inverseJoinColumns = @JoinColumn(name = "fk_user_id")
				)
	private List<User> groupMods;
	
	@ManyToMany
	@JoinTable(name = "group_applicants", 
						joinColumns = @JoinColumn(name = "fk_group_id"),
						inverseJoinColumns = @JoinColumn(name = "fk_user_id")
				)
	private List<User> groupApplicants;

	public HobbyGroup(String groupName) { 
		this.groupName = groupName; 
		this.normalMembers = new ArrayList<>();
		this.groupMods = new ArrayList<>();
		this.groupApplicants = new ArrayList<>();
//		this.images = new ArrayList<>();
	}
	public HobbyGroup() {}
	
	public Integer getGroupId() { return groupId; }
	public String getGroupName() { return groupName; }
	public String getGroupDescription() { return groupDescription; }
	public List<Project> getProjects() { return projects; }
	public List<User> getNormalMembers() { return normalMembers; }
	public List<User> getGroupMods() { return groupMods; }
	public List<User> getGroupApplicants() { return groupApplicants; }
	public void setGroupId(Integer groupId) { this.groupId = groupId; }
	public void setGroupName(String groupName) { this.groupName = groupName; }
	public void setGroupDescription(String groupDescription) { this.groupDescription = groupDescription; }
	public void setProjects(List<Project> projects) { this.projects = projects; }
	public void setNormalMembers(List<User> normalMembers) { this.normalMembers = normalMembers; }
	public void setGroupMods(List<User> groupMods) { this.groupMods = groupMods; }
	public void setGroupApplicants(List<User> groupApplicants) { this.groupApplicants = groupApplicants;}
	
	public void addProject(Project project) {projects.add(project);}
	public void addMember(User user) {normalMembers.add(user);}
	public void addModerator(User user) {groupMods.add(user);}
	public void addApplicant(User user) {groupApplicants.add(user);}
	
	public void removeProject(Project project) {projects.remove(project);}
	public void removeMember(User user) {normalMembers.remove(user); }
	public void removeModerator(User user) {groupMods.remove(user);}
	public void removeApplicant(User user) {groupApplicants.remove(user);}
}
