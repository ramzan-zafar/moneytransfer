package com.moneytransfer;
import static java.lang.Thread.sleep;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.moneytransfer.MoneyTransferAPI;
import com.moneytransfer.dao.DAOFactory;
import com.moneytransfer.dao.UserDAO;
import com.moneytransfer.dao.impl.UserDAOImpl;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.UserException;
import com.moneytransfer.model.User;

@FixMethodOrder(MethodSorters.DEFAULT)
public class UserDaoUnitTest {
	private UserDAO userDao = new UserDAOImpl();
	
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	
	@Before
	public void beforeClass() throws InterruptedException {
		h2DaoFactory.populateTestData();
	}

	@Test
	public void getAllUsers() throws UserException {
		List<User> users = userDao.getAllUsers();
		Assert.assertEquals(users.size(), 2);
	}

	@Test
	public void createNewUser() throws AlreadyExistException, NotFoundException, UserException {

		User user = User.builder().firstName("ramzan").lastName("zafar").email("ramzan@example.com").build();
		userDao.createUser(user);
		Collection<User> users = userDao.getAllUsers();
		Assert.assertEquals(users.size(), 3);

	}

	@Test
	public void updateExistingUser() throws NotFoundException, UserException {
		User user = User.builder().userId(Long.valueOf(2)).firstName("ramzan-updated").build();
		userDao.updateUser(user);
		User updatedUser = userDao.getUserById(Long.valueOf(2));
		Assert.assertEquals("ramzan-updated", updatedUser.getFirstName());
	}

}
