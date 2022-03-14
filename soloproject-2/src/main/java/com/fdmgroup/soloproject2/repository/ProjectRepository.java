package com.fdmgroup.soloproject2.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>{

	@Query("SELECT p FROM Project p ORDER BY dateOfCreation DESC")
	List<Project> findNewestProjects();

	Project findByProjectName(String projectname);

	List<Project> findByProjectNameContaining(String searchterm);

	List<Project> findByProjectDescriptionContaining(String searchterm);

	List<Project> findByProjectTopicContaining(String searchterm);

	@Query("SELECT p.projectTopic FROM Project p ORDER BY p.projectTopic")
	Collection<String> findAllTopics();
}
