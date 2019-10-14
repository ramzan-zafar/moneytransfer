package com.moneytransfer.service;

import java.util.List;

import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.UserException;
import com.moneytransfer.model.User;

public interface UserService {
	List<User> getAllUsers() throws UserException;

	User getUserById(long id) throws NotFoundException;

	boolean userExist(String email) throws UserException;
	
	long createUser(User user) throws AlreadyExistException,UserException;

	int updateUser(User user) throws UserException;

	int deleteUser(long userId) throws UserException;
}
