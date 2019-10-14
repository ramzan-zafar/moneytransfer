package com.moneytransfer.module;

import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.dao.impl.AccountDAOImpl;
import com.moneytransfer.service.AccountServiceImpl;

import dagger.Module;
import dagger.Provides;


@Module
public class AccountServiceModule {
    @Provides
    public AccountDAO provideAccountDao(){
        return new  AccountDAOImpl();
    }
    @Provides
    public AccountServiceImpl provideAccountService(){
        return new AccountServiceImpl(this.provideAccountDao());
    }
}
