package com.moneytransfer;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.moneytransfer.component.UserServiceComponent;
import com.moneytransfer.dao.DAOFactory;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.UserException;
import com.moneytransfer.model.User;
import com.moneytransfer.service.UserService;

import com.moneytransfer.component.DaggerUserServiceComponent;

public class UserServiceIntegrationTest {

	UserServiceComponent userServiceComponent = DaggerUserServiceComponent.create();

	UserService userService = userServiceComponent.buildUserService();
	
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);


	@Before
	public void beforeClass() throws InterruptedException {
		h2DaoFactory.populateTestData();
	}
	
	
	@Test
	public void getAllAccounts() throws UserException{
		List<User> users = userService.getAllUsers();
		Assert.assertEquals(users.size(), 2);
	}

	@Test
	public void createNewAccount() throws AlreadyExistException, NotFoundException, UserException {

		User user = User.builder().firstName("ramzan").lastName("zafar").email("example@gmail.com").build();
		userService.createUser(user);
		List<User> users = userService.getAllUsers();
		Assert.assertEquals(users.size(), 3);

	}

	@Test
	public void updateNewAccount() throws UserException, NotFoundException {
		User user = User.builder().userId(Long.valueOf(2)).firstName("ramzan-updated").build();
		userService.updateUser(user);

		User updatedUser = userService.getUserById(Long.valueOf(2));

		Assert.assertEquals("ramzan-updated", updatedUser.getFirstName());

	}
}
