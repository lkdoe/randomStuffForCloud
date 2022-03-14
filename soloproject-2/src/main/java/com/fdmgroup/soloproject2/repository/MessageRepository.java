package com.fdmgroup.soloproject2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.soloproject2.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	

}
