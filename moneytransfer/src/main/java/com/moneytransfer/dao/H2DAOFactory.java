package com.moneytransfer.dao;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import com.moneytransfer.util.Utils;

/**
 * H2 DAO
 */
public class H2DAOFactory extends DAOFactory {
	private static final String H2_DRIVER = Utils.getStringProperty("datasource.h2.driver");
	private static final String H2_CONNECTION_URL = Utils.getStringProperty("datasource.h2.connection.url");
	private static final String H2_USER = Utils.getStringProperty("datasource.h2.user");
	private static final String H2_PASSWORD = Utils.getStringProperty("datasource.h2.password");
	private static Logger LOG = Logger.getLogger(H2DAOFactory.class);


	H2DAOFactory() {
		DbUtils.loadDriver(H2_DRIVER);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(H2_CONNECTION_URL, H2_USER, H2_PASSWORD);

	}

	@Override
	public void populateTestData() {
		LOG.info("Populating Test User Table and data ..... ");
		Connection conn = null;
		try {
			conn = H2DAOFactory.getConnection();
			RunScript.execute(conn, new FileReader("src/test/java/resources/demo.sql"));
		} catch (SQLException e) {
			LOG.error("populateTestData(): Error populating user data: ", e);
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			LOG.error("populateTestData(): Error finding test script file ", e);
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

}
