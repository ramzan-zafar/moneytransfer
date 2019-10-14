package com.moneytransfer.dao;


import java.math.BigDecimal;
import java.util.List;

import com.moneytransfer.exception.AccountException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;


public interface AccountDAO {

    List<Account> getAllAccounts();
    Account getAccountById(long accountId) throws NotFoundException;
    long createAccount(Account account) throws AccountException;
    int deleteAccount(long accountId) throws AccountException;
    int updateAccountBalance(long accountId, BigDecimal deltaAmount) throws NotSufficientBalanceException,AccountException;
    int transferAccountBalance(UserTransaction userTransaction) throws NotSufficientBalanceException,AccountException;
}
