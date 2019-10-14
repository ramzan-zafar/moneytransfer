package com.moneytransfer.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import com.google.gson.Gson;
import com.moneytransfer.component.AccountServiceComponent;
import com.moneytransfer.component.DaggerAccountServiceComponent;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;
import com.moneytransfer.response.StandardResponse;
import com.moneytransfer.response.StatusResponse;
import com.moneytransfer.service.AccountService;

public class AccountController {
	AccountServiceComponent accountServiceComponent = DaggerAccountServiceComponent.create();
	AccountService accountService = accountServiceComponent.buildAccountService();

	public void registerAccountApiRoutes() {

		// get all accounts
		get("/accounts", (req, res) -> {
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					new Gson().toJsonTree(accountService.getAllAccounts())));
		});

		// get account details by account id
		get("/account/:id", (req, res) -> {
			String accountId = Objects.requireNonNull(req.params(":id"));
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					new Gson().toJsonTree(accountService.getAccount(Long.valueOf(accountId)))));
		});

		// get balance of account
		get("/account/:id/balance", (req, res) -> {
			String accountId = Objects.requireNonNull(req.params(":id"));
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					new Gson().toJsonTree(accountService.getAccountBalance(Long.valueOf(accountId)))));
		});

		// create a new account
		post("/account/", (req, res) -> {

			String userId = Objects.requireNonNull(req.queryParams("userId"));
			String balance = Objects.requireNonNull(req.queryParams("balance"));
			String currencyCode = Objects.requireNonNull(req.queryParams("currencyCode"));
			Account account = Account.builder().userId(Long.valueOf(userId))
					.balance(BigDecimal.valueOf(Long.valueOf(balance))).currency(Currency.getInstance(currencyCode))
					.build();
			long accountId = accountService.createAccount(account);
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					"New account has been created with accountId:" + accountId));
		});

		// delete a particular account
		delete("/account/:id", (req, res) -> {
			String accountId = Objects.requireNonNull(req.params(":id"));
			accountService.deleteAccount(Long.valueOf(accountId));
			return new Gson().toJson(
					new StandardResponse(StatusResponse.SUCCESS, "Account with %s id has been deleted", accountId));
		});

		// withdraw money from account
		put("/account/:id/withdraw/:amount", (req, res) -> {
			String accountId = Objects.requireNonNull(req.params(":id"));
			String amountToWithdraw = Objects.requireNonNull(req.params(":amount"));
			accountService.withdrawMoney(Long.valueOf(accountId), amountToWithdraw);
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					"Amount has been withdrawn from account with id %s", accountId));
		});

		// deposit money to account
		put("/account/:id/deposit/:amount", (req, res) -> {
			String accountId = Objects.requireNonNull(req.params(":id"));
			String amountToDeposit = Objects.requireNonNull(req.params(":amount"));
			accountService.depositMoney(Long.valueOf(accountId), amountToDeposit);
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
					"Amount has been deposited to account with id %s", accountId));
		});

		post("/transfermoney", (req, res) -> {
			String fromAccountId = Objects.requireNonNull(req.queryParams("fromAccountId"));
			String toAccountId = Objects.requireNonNull(req.queryParams("toAccountId"));
			String amountToTransfer = Objects.requireNonNull(req.queryParams("amountToTransfer"));
			String curreny = Objects.requireNonNull(req.queryParams("currency"));

			UserTransaction userTransaction = UserTransaction.builder().fromAccountId(Long.valueOf(fromAccountId))
					.toAccountId(Long.valueOf(toAccountId)).amount(BigDecimal.valueOf(Long.valueOf(amountToTransfer))).currency(curreny)
					.build();

			accountService.transferMoney(userTransaction);
			return new Gson()
					.toJson(new StandardResponse(StatusResponse.SUCCESS, "Money has been transferred successfully"));
		});
	}
}
