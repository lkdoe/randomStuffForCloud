package com.fdmgroup.soloproject2.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String userName);

	@Query("SELECT u.favourites FROM User u")
	Collection<? extends Project> getAllFavourites();

	List<User> findByFavourites(Project p);
	
	List<User> findByMessagesSender(User sender);
	List<User> findByMessagesSenderOrderByMessagesTimeOfWritingDesc(User sender);

}
