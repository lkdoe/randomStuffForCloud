package com.fdmgroup.soloproject2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.GroupRepository;
import com.fdmgroup.soloproject2.repository.UserRepository;

@Service
public class GroupServiceImp implements GroupService{
	private GroupRepository groupRepository;
	private UserRepository userRepository;
	
	@Autowired
	public GroupServiceImp(GroupRepository groupRepository, UserRepository userRepository) {
		this.groupRepository = groupRepository;
		this.userRepository = userRepository;
	}

	@Override
	public HobbyGroup findByGroupName(String groupName) {
		return groupRepository.findByGroupName(groupName); 
	}

	@Override
	public List<HobbyGroup> getAll() {
		return groupRepository.findAll(); 
	}

	@Override
	public List<HobbyGroup> getMyGroups(User user) {
		List<HobbyGroup> myGroups = groupRepository.findByGroupMods(user);
		myGroups.addAll(groupRepository.findByNormalMembers(user));
		return myGroups; 
	}

	@Override
	public void registerGroup(HobbyGroup group) {
		groupRepository.save(group);
	}

	@Override
	public HobbyGroup findByGroupID(Integer groupId) { 
		return groupRepository.findById(groupId).orElse(null); 
	}

	@Override
	public void abdicateAsMod(HobbyGroup group, User user) {
		if(group.getGroupMods().contains(user)) {
			group.removeModerator(user);
			group.addMember(user);
			if(group.getGroupMods().isEmpty()) {
				group.addModerator(group.getNormalMembers().get(0));
				group.removeMember(user);
			}
			groupRepository.save(group);
		}
	}

	@Override
	public String leaveGroup(HobbyGroup group, User user) {
		group.removeMember(user);
		group.removeModerator(user);
		groupRepository.save(group);
		if(group.getGroupMods().isEmpty() && group.getNormalMembers().isEmpty()) {
			for(Project p : group.getProjects()) {
				List<User> whoHasFavourite = userRepository.findByFavourites(p);
				for(User u : whoHasFavourite) {
					u.removeFavourite(p);
					userRepository.save(u);
				}
			}
			groupRepository.delete(group);
			return "/groups";
		}
		return "/group/" + group.getGroupName(); 
	}

	@Override
	public HobbyGroup findGroupOfProject(Project project) {
		return groupRepository.findByProjects(project); 
	}

	@Override
	public void addApplicant(HobbyGroup group, User user) {
		if(!group.getGroupApplicants().contains(user)) {
			group.addApplicant(user);
			groupRepository.save(group);
		}
	}

	@Override
	public void removeApplicant(HobbyGroup group, User user) {
		if(group.getGroupApplicants().contains(user)) {
			group.removeApplicant(user);
			groupRepository.save(group);
		}
	}

	@Override
	public void acceptApplication(HobbyGroup group, User moderator, User applicant) {
		if(group.getGroupMods().contains(moderator)) {
			group.addMember(applicant);
			group.removeApplicant(applicant);
			groupRepository.save(group);
		}
	}

	@Override
	public void denyApplication(HobbyGroup group, User moderator, User applicant) {
		if(group.getGroupMods().contains(moderator)) {
			group.removeApplicant(applicant);
			groupRepository.save(group);
		}
	}

	@Override
	public void promoteToMod(HobbyGroup group, User moderator, User member) {
		if(group.getGroupMods().contains(moderator)) {
			group.removeMember(member);
			group.addModerator(member);
			groupRepository.save(group);
		}
	}

	@Override
	public void throwOutOfGroup(HobbyGroup group, User moderator, User member) {
		if(group.getGroupMods().contains(moderator)) {
			group.removeMember(member);
			groupRepository.save(group);
		}
	}
	
	
}
