package com.siebre.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.siebre.domain.User;

@Repository
public interface UserDao {

	List<User> findAll();

	User getUserById(String id);

	void deleteUserById(String id);

}
