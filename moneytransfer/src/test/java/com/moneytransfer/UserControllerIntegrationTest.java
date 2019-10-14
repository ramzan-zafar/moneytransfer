package com.moneytransfer;
import com.google.gson.JsonElement;
import com.moneytransfer.MoneyTransferAPI;
import com.moneytransfer.dao.DAOFactory;

import helper.TestResponse;
import helper.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class UserControllerIntegrationTest {

	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	
	@Before
	public void beforeClass() throws InterruptedException {
		MoneyTransferAPI.main(null);
		sleep(3000);
		
		h2DaoFactory.populateTestData();
	}

	@Test
	public void getListOfAllUsers() {
		TestResponse res = Util.request("GET", "/users");
		JsonElement json = res.jsonElement();
		Assert.assertEquals(200, res.status);
		assertEquals(2, json.getAsJsonObject().size());
	}

	@Test
	public void aNewUserShouldBeCreated() {
		TestResponse res = Util.request("POST", "/user/3?firstName=ramzan&lastName=zafar&email=ramzan@example.com");
		Assert.assertEquals(200, res.status);
	}

	@Test
	public void anExistingUserShouldBeUpdated() {
		try {
			TestResponse res = Util.request("PUT", "/user/13?firstName=ramzan&lastName=zafar");
			JsonElement json = res.jsonElement();
			Assert.assertEquals(200, res.status);
			assertEquals("zafar", json.getAsJsonObject().get("data").getAsJsonObject().get("lastName").getAsString());
			assertEquals("ramzan@example.com",
					json.getAsJsonObject().get("data").getAsJsonObject().get("email").getAsString());
		} finally {
			Util.request("DELETE", "/user/3");
		}
	}

	@Test
	public void anExistingUserShouldBeDeleted() {

		TestResponse res = Util.request("DELETE", "/user/2");
		Assert.assertEquals(200, res.status);

	}
}
