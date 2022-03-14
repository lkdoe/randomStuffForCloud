package com.fdmgroup.soloproject2.service;

import java.util.List;

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;

public interface GroupService {

	HobbyGroup findByGroupName(String groupName);

	List<HobbyGroup> getAll();

	List<HobbyGroup> getMyGroups(User user);

	void registerGroup(HobbyGroup group);

	HobbyGroup findByGroupID(Integer groupId);

	void abdicateAsMod(HobbyGroup group, User user);

	String leaveGroup(HobbyGroup group, User user);

	HobbyGroup findGroupOfProject(Project project);

	void addApplicant(HobbyGroup group, User user);

	void removeApplicant(HobbyGroup group, User user);

	void acceptApplication(HobbyGroup group, User moderator, User applicant);

	void denyApplication(HobbyGroup group, User moderator, User applicant);

	void promoteToMod(HobbyGroup group, User moderator, User member);

	void throwOutOfGroup(HobbyGroup group, User moderator, User member);

}
