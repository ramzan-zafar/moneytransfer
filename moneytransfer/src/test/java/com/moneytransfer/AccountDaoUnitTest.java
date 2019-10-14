package com.moneytransfer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.dao.DAOFactory;
import com.moneytransfer.dao.impl.AccountDAOImpl;
import com.moneytransfer.exception.AccountException;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.User;
import com.moneytransfer.model.UserTransaction;

@FixMethodOrder(MethodSorters.DEFAULT)
public class AccountDaoUnitTest {
	
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
	
	private AccountDAO accountDao = new AccountDAOImpl();

	@Before
	public void depositMoneyIntoAccount() throws NotFoundException, NotSufficientBalanceException, AccountException {
		h2DaoFactory.populateTestData();
	}

	@Test
	public void getAllAccounts() {
		List<Account> users = accountDao.getAllAccounts();
		Assert.assertEquals(2, users.size());
	}

	@Test
	public void createNewAccount() throws AlreadyExistException, NotFoundException, AccountException {

		Account account = Account.builder().balance(BigDecimal.valueOf(20)).currency(Currency.getInstance("CAD"))
				.userId(Long.valueOf(1)).build();
		accountDao.createAccount(account);
		Collection<Account> accounts = accountDao.getAllAccounts();
		Assert.assertEquals(3, accounts.size());

	}

	@Test
	public void depositMoneyShouldBeSuccessfull()
			throws NotFoundException, NotSufficientBalanceException, NumberFormatException, AccountException {

		Account accountBefore = accountDao.getAccountById(Long.valueOf("1"));
		accountDao.updateAccountBalance(Long.valueOf("1"), BigDecimal.valueOf(50));
		BigDecimal moneyInAccountAfterExpected = accountBefore.getBalance().add(BigDecimal.valueOf(50));
		Account accountAfter = accountDao.getAccountById(Long.valueOf("1"));
		Assert.assertEquals(moneyInAccountAfterExpected, accountAfter.getBalance());

	}

	@Test
	public void withdrawMoneyShouldBeSuccessfull()
			throws NotFoundException, NotSufficientBalanceException, NumberFormatException, AccountException {
		Account accountBefore = accountDao.getAccountById(Long.valueOf("1"));
		accountDao.updateAccountBalance(Long.valueOf("1"), BigDecimal.valueOf(50).negate());
		BigDecimal moneyInAccountAfterExpected = accountBefore.getBalance().subtract(BigDecimal.valueOf(50));
		Account accountAfter = accountDao.getAccountById(Long.valueOf("1"));
		Assert.assertEquals(moneyInAccountAfterExpected, accountAfter.getBalance());
	}

	@Test
	public void moneyTransferShouldBeSuccessfull() throws NotFoundException, NotSufficientBalanceException, AccountException {

		Account account1Before = accountDao.getAccountById(Long.valueOf("1"));
		Account account2Before = accountDao.getAccountById(Long.valueOf("2"));

		UserTransaction userTransaction = UserTransaction.builder().fromAccountId(account1Before.getAccountId())
				.toAccountId(account2Before.getAccountId()).amount(BigDecimal.valueOf(5)).currency("USD").build();
		Account account1After = accountDao.getAccountById(Long.valueOf("1"));
		Account account2After = accountDao.getAccountById(Long.valueOf("2"));

		accountDao.transferAccountBalance(userTransaction);
		BigDecimal moneyFromAccountExpected = account1Before.getBalance().subtract(BigDecimal.valueOf(5));
		Assert.assertEquals(moneyFromAccountExpected, account1After.getBalance());
		BigDecimal moneyToAccountBeforeExpected = account2Before.getBalance().add(BigDecimal.valueOf(5));
		Assert.assertEquals(moneyToAccountBeforeExpected, account2After.getBalance());

	}
}
