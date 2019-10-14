package com.moneytransfer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.moneytransfer.dao.H2DAOFactory;
import com.moneytransfer.dao.UserDAO;
import com.moneytransfer.exception.AlreadyExistException;
import com.moneytransfer.exception.NotFoundException;
import com.moneytransfer.exception.UserException;
import com.moneytransfer.model.User;

public class UserDAOImpl implements UserDAO {

	private static Logger log = Logger.getLogger(UserDAOImpl.class);
	private final static String SQL_GET_USER_BY_ID = "SELECT * FROM User WHERE user_id = ? ";
	private final static String SQL_GET_ALL_USERS = "SELECT * FROM User";
	private final static String SQL_GET_USER_BY_EMAIL = "SELECT email FROM User WHERE email = ? ";
	private final static String SQL_INSERT_USER = "INSERT INTO User (first_name, last_name,email) VALUES (?, ?, ?)";
	private final static String SQL_UPDATE_USER = "UPDATE User SET first_name = ?,last_name = ? WHERE user_id = ? ";
	private final static String SQL_DELETE_USER_BY_ID = "DELETE FROM User WHERE user_id = ? ";

	/**
	 * Find all users
	 */
	@Override
	public List<User> getAllUsers() throws UserException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<User> users = new ArrayList<User>();
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_ALL_USERS);
			rs = stmt.executeQuery();
			while (rs.next()) {
				User u = User.builder().userId(rs.getLong("user_id")).firstName(rs.getString("first_name"))
						.lastName(rs.getString("last_name")).email(rs.getString("email")).build();
				users.add(u);
				if (log.isDebugEnabled())
					log.debug("getAllUsers() Retrieve User: " + u);
			}
			return users;
		} catch (SQLException e) {
			throw new UserException("Error reading user data", e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, rs);
		}
	}

	/**
	 * Find user by email
	 */
	public User getUserById(long userId) throws NotFoundException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_USER_BY_ID);
			stmt.setLong(1, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				user = User.builder().userId(rs.getLong("user_id")).firstName(rs.getString("first_name"))
						.lastName(rs.getString("last_name")).email(rs.getString("email")).build();
				if (log.isDebugEnabled())
					log.debug("getUserById(): Retrieve User: " + user);
			}
			return user;
		} catch (SQLException e) {
			throw new NotFoundException("Error reading user data", e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, rs);
		}
	}

	/**
	 * Save User
	 * @throws UserException
	 */
	@Override
	public long createUser(User user) throws AlreadyExistException, UserException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;
		try {
			if (this.userExist(user.getEmail())) {
				throw new AlreadyExistException("User with email{" + user.getEmail() + "} already exist");
			}
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getEmail());
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				log.error("insertUser(): Creating user failed, no rows affected." + user);
				throw new UserException("Users Cannot be created");
			}
			generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				log.error("insertUser():  Creating user failed, no ID obtained." + user);
				throw new UserException("Users Cannot be created");
			}
		} catch (SQLException e) {
			log.error("Error Inserting User :" + user);
			throw new UserException("Error creating user data", e);
		} finally {
			DbUtils.closeQuietly(conn, stmt, generatedKeys);
		}

	}

	/**
	 * Update User
	 * 
	 * @throws UserException
	 */
	@Override
	public int updateUser(User user) throws UserException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_UPDATE_USER);
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setLong(3, user.getUserId());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error Updating User :" + user);
			throw new UserException("Error update user data", e);
		} finally {
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);

		}
	}

	/**
	 * Delete User
	 */
	@Override
	public int deleteUser(long userId) throws UserException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_DELETE_USER_BY_ID);
			stmt.setLong(1, userId);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error Deleting User :" + userId);
			throw new UserException("Error Deleting User ID:" + userId, e);
		} finally {
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);

		}
	}

	@Override
	public boolean userExist(String email) throws UserException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rsUser = null;
		boolean isUserExist = false;
		try {
			conn = H2DAOFactory.getConnection();
			stmt = conn.prepareStatement(SQL_GET_USER_BY_EMAIL);
			stmt.setString(1, email);
			rsUser = stmt.executeQuery();
			if (rsUser.next()) {
				isUserExist = true;
			}
		} catch (SQLException e) {
			log.error("Error while executing userExist with email" + email);
			throw new UserException("Error while executing userExist with email:" + email, e);
		} finally {
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);
		}
		return isUserExist;
	}

}
