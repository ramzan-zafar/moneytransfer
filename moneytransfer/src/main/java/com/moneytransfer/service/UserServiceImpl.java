package com.moneytransfer.service;

import java.util.List;

import javax.inject.Inject;

import com.moneytransfer.dao.UserDAO;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.UserException;
import com.moneytransfer.model.User;

public class UserServiceImpl implements UserService {
	
	private UserDAO userDao;

	@Inject
	public UserServiceImpl(final UserDAO userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> getAllUsers() throws UserException {
		return userDao.getAllUsers();
	}

	@Override
	public User getUserById(long userId) throws NotFoundException {
		return userDao.getUserById(userId);
	}

	@Override
	public long createUser(User user) throws AlreadyExistException, UserException {
		return userDao.createUser(user);
	}

	@Override
	public int updateUser(User user) throws UserException {

		return userDao.updateUser(user);
	}

	@Override
	public int deleteUser(long userId) throws UserException {

		return userDao.deleteUser(userId);
	}

	@Override
	public boolean userExist(String email) throws UserException {

		return userDao.userExist(email);
	}
}
