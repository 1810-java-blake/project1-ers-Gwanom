package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		String url;
		String port;
		String dbName;
		String dbSchema;
		String username;
		String password;
		if ("USE_PROPERTY".equals(System.getProperty("USE_PROPERTY"))) {
			url = System.getProperty("db_url");
			port = System.getProperty("db_port");
			dbName = System.getProperty("db_name");
			dbSchema = System.getProperty("db_schema");
			username = System.getProperty("db_username");
			password = System.getProperty("db_password");

		} else {
			url = System.getenv("db_url");
			port = System.getenv("db_port");
			dbName = System.getenv("db_name");
			dbSchema = System.getenv("db_schema");
			username = System.getenv("db_username");
			password = System.getenv("db_password");
		}

		String dataSource = "jdbc:postgresql://" + url + ":" + port + "/" + dbName + "?currentSchema=" + dbSchema;

		return DriverManager.getConnection(dataSource, username, password);
	}
}
