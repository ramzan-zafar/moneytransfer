package com.moneytransfer.component;

import javax.inject.Singleton;

import com.moneytransfer.service.AccountServiceImpl;

import dagger.Component;

@Singleton
@Component(modules = com.moneytransfer.module.AccountServiceModule.class)
public interface AccountServiceComponent {
    AccountServiceImpl buildAccountService();
}
