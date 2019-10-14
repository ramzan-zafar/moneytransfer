package com.moneytransfer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.moneytransfer.component.AccountServiceComponent;
import com.moneytransfer.component.DaggerAccountServiceComponent;
import com.moneytransfer.dao.DAOFactory;
import com.moneytransfer.exception.AccountException;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.InvalidAmountException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;
import com.moneytransfer.service.AccountService;

public class AccountServiceIntegrationTest {

	AccountServiceComponent accountServiceComponent = DaggerAccountServiceComponent.create();
	AccountService accountService = accountServiceComponent.buildAccountService();


	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
	
	@Before
	public void setUp() {
		h2DaoFactory.populateTestData();
	}
	
	@Test
	public void getAllAccounts() {
		List<Account> accounts = accountService.getAllAccounts();
		Assert.assertEquals(6,accounts.size());
	}

	@Test
	public void createNewAccount() throws AlreadyExistException, NotFoundException, AccountException {

		Account account = Account.builder().userId(Long.valueOf("2")).currency(Currency.getInstance("CAD"))
				.balance(BigDecimal.valueOf(50)).build();
		accountService.createAccount(account);
		List<Account> accounts = accountService.getAllAccounts();
		Assert.assertEquals(7,accounts.size());

	}

	@Test
	public void deleteAccount() throws AccountException {

		accountService.deleteAccount(Long.valueOf("2"));
		Collection<Account> accounts = accountService.getAllAccounts();
		Assert.assertEquals(5, accounts.size());

	}

	@Test
	public void withdrawMoneyFromAccount() throws NotSufficientBalanceException, NotFoundException, AccountException {

		BigDecimal amountBeforeWithdrawl = accountService.getAccountBalance(Long.valueOf("2"));
		accountService.withdrawMoney(Long.valueOf("2"), "10");
		Assert.assertEquals(amountBeforeWithdrawl.subtract(BigDecimal.valueOf(10)),
				accountService.getAccountBalance(Long.valueOf("2")));

	}

	@Test
	public void depositMoneyFromAccount() throws NotSufficientBalanceException, NotFoundException, NumberFormatException, AccountException {
		
			BigDecimal amountBeforeDeposit = accountService.getAccountBalance(Long.valueOf("2"));
			accountService.depositMoney(Long.valueOf("2"), "10");
			Assert.assertEquals(amountBeforeDeposit.add(BigDecimal.valueOf(10)), accountService.getAccountBalance(Long.valueOf("2")));
		
	}

	@Test
	public void transferMoneyBetweenAccount()
			throws NotSufficientBalanceException, NotFoundException, InvalidAmountException, AccountException {

		BigDecimal amountFromAccountBeforeDeposit = accountService.getAccountBalance(Long.valueOf("1"));
		BigDecimal amountToAccountBeforeDeposit = accountService.getAccountBalance(Long.valueOf("2"));

		UserTransaction userTransaction = UserTransaction.builder().fromAccountId(Long.valueOf("1"))
				.toAccountId(Long.valueOf("2")).amount(BigDecimal.valueOf(10)).currency("USD").build();

		accountService.transferMoney(userTransaction);
		Assert.assertEquals(amountFromAccountBeforeDeposit.subtract(BigDecimal.valueOf(10)),
				accountService.getAccountBalance(Long.valueOf("1")));
		Assert.assertEquals(amountToAccountBeforeDeposit.add(BigDecimal.valueOf(10)),
				accountService.getAccountBalance(Long.valueOf("2")));

	}
}
