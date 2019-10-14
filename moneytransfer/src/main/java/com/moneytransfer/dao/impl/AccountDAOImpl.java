package com.moneytransfer.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.dao.H2DAOFactory;
import com.moneytransfer.exception.AccountException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.NotSufficientBalanceException;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.UserTransaction;

public class AccountDAOImpl implements AccountDAO {

	private static Logger log = Logger.getLogger(AccountDAOImpl.class);
	private final static String SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE account_id = ? ";
	private final static String SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE account_id = ? FOR UPDATE";
	private final static String SQL_CREATE_ACC = "INSERT INTO Account (user_id, balance, currency_code) VALUES (?, ?, ?)";
	private final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET balance = ? WHERE account_id = ? ";
	private final static String SQL_GET_ALL_ACC = "SELECT * FROM Account";
	private final static String SQL_DELETE_ACC_BY_ID = "DELETE FROM Account WHERE account_id = ?";

	/**
	 * Get all accounts.
	 */
	public List<Account> getAllAccounts() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Account> allAccounts = new ArrayList<Account>();
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_ALL_ACC);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Account acc = Account.builder().accountId(rs.getLong("account_id")).userId(rs.getLong("user_id"))
						.balance(rs.getBigDecimal("balance"))
						.currency(Currency.getInstance(rs.getString("currency_code"))).build();

				if (log.isDebugEnabled()) {
					log.debug("getAllAccounts(): Get  Account " + acc);
				}
				allAccounts.add(acc);
			}

		} catch (SQLException e) {
			// throw new CustomException("getAccountById(): Error reading account data", e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, rs);
		}
		return allAccounts;
	}

	/**
	 * Get account by id
	 */
	public Account getAccountById(long accountId) throws NotFoundException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Account acc = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_ACC_BY_ID);
			stmt.setLong(1, accountId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				acc = Account.builder().accountId(rs.getLong("account_id")).userId(rs.getLong("user_id"))
						.balance(rs.getBigDecimal("balance"))
						.currency(Currency.getInstance(rs.getString("currency_code"))).build();
				if (log.isDebugEnabled())
					log.debug("Retrieve Account By Id: " + acc);
			}
			return acc;
		} catch (SQLException e) {
			throw new NotFoundException("getAccountById(): Error reading account data", e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, rs);
		}

	}

	/**
	 * Create account
	 */
	public long createAccount(Account account) throws AccountException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_CREATE_ACC,Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, account.getUserId());
			stmt.setBigDecimal(2, account.getBalance());
			stmt.setString(3, account.getCurrency().toString());
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				log.error("createAccount(): Creating account failed, no rows affected.");
				throw new AccountException("Account Cannot be created");
			}
			generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				log.error("Creating account failed, no ID obtained.");
				throw new AccountException("Account Cannot be created");
			}
		} catch (SQLException e) {
			log.error("Error Inserting Account  " + account);
			throw new AccountException("createAccount(): Error creating user account " + account, e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, generatedKeys);
		}
	}

	/**
	 * Delete account by id
	 */
	public int deleteAccountById(long accountId) throws AccountException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_DELETE_ACC_BY_ID);
			stmt.setLong(1, accountId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AccountException("deleteAccountById(): Error deleting user account Id " + accountId, e);
		} finally {
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(stmt);
		}
	}

	/**
	 * Update account balance
	 * 
	 * @throws NotSufficientBalanceException
	 */
	@Override
	public int updateAccountBalance(long accountId, BigDecimal deltaAmount)
			throws AccountException, NotSufficientBalanceException {
		Connection conn = null;
		PreparedStatement lockStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rs = null;
		Account targetAccount = null;
		int updateCount = -1;
		try {
			conn = H2DAOFactory.getConnection();
			conn.setAutoCommit(false);
			// lock account for writing:
			lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
			lockStmt.setLong(1, accountId);
			rs = lockStmt.executeQuery();
			if (rs.next()) {
				targetAccount = Account.builder().accountId(rs.getLong("account_id")).userId(rs.getLong("user_id"))
						.balance(rs.getBigDecimal("balance"))
						.currency(Currency.getInstance(rs.getString("currency_code"))).build();
				if (log.isDebugEnabled())
					log.debug("updateAccountBalance from Account: " + targetAccount);
			}

			if (targetAccount == null) {
				throw new AccountException("updateAccountBalance(): fail to lock account : " + accountId);
			}
			// update account upon success locking
			BigDecimal balance = targetAccount.getBalance().add(deltaAmount);
			if (balance.compareTo(BigDecimal.ZERO) < 0) {
				throw new NotSufficientBalanceException("Not sufficient Fund for account: " + accountId);
			}

			updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
			updateStmt.setBigDecimal(1, balance);
			updateStmt.setLong(2, accountId);
			updateCount = updateStmt.executeUpdate();
			conn.commit();
			if (log.isDebugEnabled())
				log.debug("New Balance after Update: " + targetAccount);
			return updateCount;
		} catch (SQLException se) {
			// rollback transaction if exception occurs
			log.error("updateAccountBalance(): User Transaction Failed, rollback initiated for: " + accountId, se);
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException re) {
				throw new AccountException("Fail to rollback transaction", re);
			}
		} finally {
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(lockStmt);
			DbUtils.closeQuietly(updateStmt);
		}
		return updateCount;
	}

	/**
	 * Transfer balance between two accounts.
	 * @throws NotSufficientBalanceException 
	 */
	@Override
	public int transferAccountBalance(UserTransaction userTransaction) throws AccountException, NotSufficientBalanceException {
		int result = -1;
		Connection conn = null;
		PreparedStatement lockStmtFrom = null;
		PreparedStatement lockStmtTo = null;
		PreparedStatement updateStmt = null;
		ResultSet rsFrom = null;
		ResultSet rsTo = null;
		Account fromAccount = null;
		Account toAccount = null;

		try {
			conn = H2DAOFactory.getConnection();
			conn.setAutoCommit(false);
			// lock the credit and debit account for writing:
			lockStmtFrom = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
			lockStmtFrom.setLong(1, userTransaction.getFromAccountId());
			rsFrom = lockStmtFrom.executeQuery();
			if (rsFrom.next()) {
				fromAccount = Account.builder().accountId(rsFrom.getLong("account_id")).userId(rsFrom.getLong("user_id"))
						.balance(rsFrom.getBigDecimal("balance"))
						.currency(Currency.getInstance(rsFrom.getString("currency_code"))).build();
				if (log.isDebugEnabled())
					log.debug("transferAccountBalance from Account: " + fromAccount);
			}
			lockStmtTo = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
			lockStmtTo.setLong(1, userTransaction.getToAccountId());
			rsTo = lockStmtTo.executeQuery();
			if (rsTo.next()) {
				toAccount = Account.builder().accountId(rsTo.getLong("account_id")).userId(rsTo.getLong("user_id"))
						.balance(rsTo.getBigDecimal("balance"))
						.currency(Currency.getInstance(rsTo.getString("currency_code"))).build();
				if (log.isDebugEnabled())
					log.debug("transferAccountBalance to Account: " + toAccount);
			}

			// check locking status
			if (fromAccount == null || toAccount == null) {
				throw new AccountException("Fail to lock both accounts for write");
			}

			// check transaction currency
			if (!fromAccount.getCurrency().equals(Currency.getInstance(userTransaction.getCurrency()))) {
				throw new AccountException(
						"Fail to transfer Fund, transaction currency are different from source/destination");
			}

			// check if currency is the same for both accounts
			if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
				throw new AccountException(
						"Fail to transfer Fund, the source and destination account are in different currency");
			}

			// check enough fund in source account
			BigDecimal fromAccountLeftOver = fromAccount.getBalance().subtract(userTransaction.getAmount());
			if (fromAccountLeftOver.compareTo(BigDecimal.ZERO) < 0) {
				throw new NotSufficientBalanceException("Not enough Fund from source Account ");
			}
			// proceed with update
			updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
			updateStmt.setBigDecimal(1, fromAccountLeftOver);
			updateStmt.setLong(2, userTransaction.getFromAccountId());
			updateStmt.addBatch();
			
			updateStmt.setBigDecimal(1, toAccount.getBalance().add(userTransaction.getAmount()));
			updateStmt.setLong(2, userTransaction.getToAccountId());
			updateStmt.addBatch();
			
			int[] rowsUpdated = updateStmt.executeBatch();
			result = rowsUpdated[0] + rowsUpdated[1];
			if (log.isDebugEnabled()) {
				log.debug("Number of rows updated for the transfer : " + result);
			}
			// If there is no error, commit the transaction
			conn.commit();
		} catch (SQLException se) {
			// rollback transaction if exception occurs
			log.error("transferAccountBalance(): User Transaction Failed, rollback initiated for: " + userTransaction,se);
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException re) {
				throw new AccountException("Fail to rollback transaction", re);
			}
		} finally {
			DbUtils.closeQuietly(rsFrom);
			DbUtils.closeQuietly(rsTo);
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(lockStmtFrom);
			DbUtils.closeQuietly(lockStmtTo);
			DbUtils.closeQuietly(conn);
		}
		return result;
	}

	@Override
	public int deleteAccount(long accountId) throws AccountException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_DELETE_ACC_BY_ID);
			stmt.setLong(1, accountId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error Deleting account :" + accountId);
			throw new AccountException("Error Deleting account ID:" + accountId, e);
		} finally {
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);

		}
	}
}
