package com.moneytransfer.component;

import javax.inject.Singleton;

import com.moneytransfer.module.UserServiceModule;
import com.moneytransfer.service.UserServiceImpl;

import dagger.Component;

@Singleton
@Component(modules = UserServiceModule.class)
public interface UserServiceComponent {
    UserServiceImpl buildUserService();
}
