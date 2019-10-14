package com.moneytransfer.service;

import java.math.BigDecimal;
import java.util.List;

import com.moneytransfer.exception.AccountException;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;

public interface AccountService {
    List<Account> getAllAccounts();
    Account getAccount(long accountId) throws NotFoundException;
    BigDecimal getAccountBalance(long accoundId) throws NotFoundException;
    int deleteAccount(long accoundId) throws AccountException;
    long createAccount(Account account) throws AlreadyExistException, AccountException ;
    void withdrawMoney(long accountId, String amountToWithdraw) throws NotFoundException, NotSufficientBalanceException,AccountException;
    void depositMoney(long accountId, String amountToDepsit) throws NotFoundException, NotSufficientBalanceException,AccountException;
    void transferMoney(UserTransaction userTransaction) throws NotSufficientBalanceException, NotFoundException,AccountException;
}
