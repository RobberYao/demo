package com.siebre.service;

import java.util.List;

import com.siebre.domain.User;

public interface UserService {

	List<User> findAll();
	
	User getUserById(String id);

	void deleteUserById(String id);

}
