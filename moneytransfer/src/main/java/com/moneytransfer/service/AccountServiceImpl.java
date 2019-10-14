package com.moneytransfer.service;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.exception.AccountException;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;


public class AccountServiceImpl implements AccountService {
    private AccountDAO accountDao;
    
    @Inject
    public AccountServiceImpl(final AccountDAO accountDao) {
        this.accountDao = accountDao;
    }
    @Override
    public Account getAccount(final long accountId) throws NotFoundException {
        return accountDao.getAccountById(accountId);
    }

    @Override
    public BigDecimal getAccountBalance(final long accountId) throws NotFoundException {
    	
    		BigDecimal balance = BigDecimal.ZERO;
    		
        Account account = accountDao.getAccountById(accountId);
        
        if(account!=null) {
        		balance = account.getBalance();
        }
        	
        	return balance;
    }

    @Override
    public int deleteAccount(final long accountId) throws AccountException {
       return accountDao.deleteAccount(accountId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @Override
    public long createAccount(final Account account) throws AlreadyExistException, AccountException {
    return 	accountDao.createAccount(account);
       
    }

    @Override
    public void withdrawMoney(final long accountId, final String amountToWithdraw) throws NotFoundException, NotSufficientBalanceException, AccountException {
        BigDecimal amountToWithdrawInDecimal = BigDecimal.valueOf(Long.valueOf(amountToWithdraw));
        accountDao.updateAccountBalance(accountId, amountToWithdrawInDecimal.negate());
    }

    @Override
    public void depositMoney(final long accountId, final String amountTodeposit) throws NotFoundException, NotSufficientBalanceException, AccountException {
        BigDecimal amountToWithdrawInDecimal = BigDecimal.valueOf(Long.valueOf(amountTodeposit));
        accountDao.updateAccountBalance(accountId, amountToWithdrawInDecimal);
    }

    @Override
    public void transferMoney(UserTransaction userTransaction) throws NotSufficientBalanceException, NotFoundException, AccountException {
  
        accountDao.transferAccountBalance(userTransaction);
    }
}
