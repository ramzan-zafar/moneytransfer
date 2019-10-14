package com.moneytransfer;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.exception.AccountException;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.InvalidAmountException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.exception.SameAccountException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;
import com.moneytransfer.service.AccountServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {
    AccountDAO accountDao = mock(AccountDAO.class);
    Account account = mock(Account.class);
    
    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void callDaoGetUsersWhenServiceGetUsersIsCalled(){
        given(accountDao.getAllAccounts()).willReturn(null);
        accountServiceImpl.getAllAccounts();
        verify(accountDao,times(1)).getAllAccounts();
    }
    @Test
    public void callDaoGetUserByIdWhenServiceGetUserByIdIsCalled() throws NotFoundException {
        given(accountDao.getAccountById(Long.valueOf(1))).willReturn(account);
        accountServiceImpl.getAccount(Long.valueOf(1));
        verify(accountDao,times(1)).getAccountById(Long.valueOf(1));
    }
    @Test
    public void callDaoCreateUserWhenServiceCreateUserByIdIsCalled() throws AlreadyExistException, AccountException {
        accountServiceImpl.createAccount(account);
        verify(accountDao,times(1)).createAccount(account);
    }

    @Test
    public void callDaoDepositMoneyWhenServiceDepositMoneyIsCalled() throws NotFoundException, NotSufficientBalanceException, AccountException {
        accountServiceImpl.depositMoney(Long.valueOf(1), "10");
        verify(accountDao,times(1)).updateAccountBalance(Long.valueOf(1), BigDecimal.valueOf(10));
    }

    @Test
    public void callDaoWithdrawMoneyWhenServiceWithdrawMoneyIsCalled() throws NotFoundException, NotSufficientBalanceException, AccountException {
        accountServiceImpl.depositMoney(Long.valueOf(1), "10");
        verify(accountDao,times(1)).updateAccountBalance(Long.valueOf(1), BigDecimal.valueOf(10).negate());
    }

    @Test
    public void callDaoMakePaymentWhenServiceMakePaymentIsCalled() throws NotFoundException, NotSufficientBalanceException, SameAccountException, InvalidAmountException, AccountException {
        
    	UserTransaction usTransaction = UserTransaction.builder().fromAccountId(Long.valueOf(1)).toAccountId(Long.valueOf(2)).currency("USD").amount(BigDecimal.valueOf(50)).build();
    	accountServiceImpl.transferMoney(usTransaction);
        verify(accountDao,times(1)).transferAccountBalance(usTransaction);
    }
}
