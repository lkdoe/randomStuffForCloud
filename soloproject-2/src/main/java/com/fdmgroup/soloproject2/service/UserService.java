package com.fdmgroup.soloproject2.service;

import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;

public interface UserService {
	User findByUserName(String userName);

	void registerUser(User user);

	void addToFavourites(Project project, User user);

	void removeFromFavourites(Project project, User user);

	User findByUserId(Integer userId);

}
