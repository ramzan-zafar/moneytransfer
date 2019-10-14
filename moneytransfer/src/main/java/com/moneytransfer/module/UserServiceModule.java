package com.moneytransfer.module;

import com.moneytransfer.dao.UserDAO;
import com.moneytransfer.dao.impl.UserDAOImpl;
import com.moneytransfer.service.UserServiceImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class UserServiceModule {
    @Provides
    public UserDAO provideUserDao(){
        return new UserDAOImpl();
    }
    @Provides
    public UserServiceImpl provideUserService(){
        return new UserServiceImpl(this.provideUserDao());
    }
}
