package main.java.com.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	public static String url = "jdbc:sqlite:chatdb.db";

	public DBManager() {
		super();
	}
	
	public static Connection connect() {
		Connection connection = null;
		try {
			connection =  DriverManager.getConnection(url);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
}
