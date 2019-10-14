package com.moneytransfer;
import com.google.gson.JsonElement;
import com.moneytransfer.MoneyTransferAPI;
import com.moneytransfer.dao.DAOFactory;

import helper.TestResponse;
import helper.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class AccountControllerIntegrationTest {
	
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@Before
	public void beforeClass() throws InterruptedException {
		MoneyTransferAPI.main(null);
		sleep(3000);
		h2DaoFactory.populateTestData();
	}

	@Test
	public void getListOfAllAccounts() {
		TestResponse res = Util.request("GET", "/accounts");
		JsonElement json = res.jsonElement();
		Assert.assertEquals(200, res.status);
		assertEquals(6, json.getAsJsonObject().size());
	}

	@Test
	public void testNewAccountShouldBeCreated() {
		TestResponse res = Util.request("PUT", "/account/7?userId=1&balance=200&currencyCode=EUR");
		Assert.assertEquals(200, res.status);
	}

	@Test
	public void testAmountCanBeDepositedInAnAccount() {
		TestResponse res = Util.request("PUT", "/account/2/deposit/20");
		Assert.assertEquals(200, res.status);
	}

	@Test
	public void testAmountCanBeWithdrawnFromAnAccount() {
		TestResponse res = Util.request("PUT", "/account/2/withdraw/20");
		Assert.assertEquals(200, res.status);
	}

	@Test
	public void balanceForAnAccountShouldBeCorrect() {
		TestResponse res = Util.request("GET", "/account/2/balance");
		JsonElement json = res.jsonElement();
		Assert.assertEquals(200, res.status);
		assertEquals(BigDecimal.valueOf(200), json.getAsJsonObject().get("data").getAsBigDecimal());
	}

	@Test
	public void anExistingAccountShouldBeDeleted() {

		TestResponse res = Util.request("DELETE", "/account/2");
		Assert.assertEquals(200, res.status);

	}
}
