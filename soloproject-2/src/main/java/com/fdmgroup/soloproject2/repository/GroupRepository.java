package com.fdmgroup.soloproject2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
@Repository
public interface GroupRepository extends JpaRepository<HobbyGroup, Integer> {

	HobbyGroup findByGroupName(String groupName);
	
	List<HobbyGroup> findByGroupMods(User user);

	List<HobbyGroup> findByNormalMembers(User user);

	HobbyGroup findByProjects(Project project);
}
