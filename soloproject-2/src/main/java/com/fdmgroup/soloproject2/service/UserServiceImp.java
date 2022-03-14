package com.fdmgroup.soloproject2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImp(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName).orElse(null);
	}

	@Override
	public void registerUser(User user) {
		userRepository.save(user);
	}

	@Override
	public void addToFavourites(Project project, User user) {
		user.addFavourite(project);
		userRepository.saveAndFlush(user);
	}
	
	@Override
	public void removeFromFavourites(Project project, User user) {
		if(user.getFavourites().contains(project)) {
			user.removeFavourite(project);
			userRepository.save(user);
		}
	}

	@Override
	public User findByUserId(Integer userId) {
		return userRepository.findById(userId).orElse(null); 
	}
}
