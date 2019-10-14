package com.moneytransfer;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.moneytransfer.dao.UserDAO;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.UserException;
import com.moneytransfer.model.User;
import com.moneytransfer.service.UserServiceImpl;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {
	
	@Mock
    UserDAO userDao;
    
	@Mock
    User user;
    
	@Mock
    List<User> users;
    
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    public void callDaoGetUsersWhenServiceGetUsersIsCalled() throws UserException{
        given(userDao.getAllUsers()).willReturn(users);
        userServiceImpl.getAllUsers();
        verify(userDao,times(1)).getAllUsers();
    }
    @Test
    public void callDaoGetUserByIdWhenServiceGetUserByIdIsCalled() throws NotFoundException {
        given(userDao.getUserById(Long.valueOf(1))).willReturn(user);
        userServiceImpl.getUserById(Long.valueOf(1));
        verify(userDao,times(1)).getUserById(Long.valueOf(1));
    }
    @Test
    public void callDaoCreateUserWhenServiceCreateUserByIdIsCalled() throws AlreadyExistException, UserException {
        userServiceImpl.createUser(user);
        verify(userDao,times(1)).createUser(user);
    }
 
}
