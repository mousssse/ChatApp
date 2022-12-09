package main.java.com.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private static final DBManager manager = new DBManager();

	// TODO : concatenate with path of app: "jdbc:sqlite:" + path + "/sqlite/db/chatApp.db"
	private String url;
	
	private Connection conn;

	private DBManager() {
		super();
		this.url = "jdbc:sqlite:chatApp.db";
		this.initTables();
	}
	
	public static DBManager getInstance() {
		return manager;
	}
	
	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn =  DriverManager.getConnection(this.url);			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void initTables () {
        try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
                + " messageId INT PRIMARY KEY, \n"
                + " content TEXT NOT NULL, \n"
                + " time TEXT NOT NULL, \n"
                + " from_id TEXT NOT NULL, \n"
                + " to_id TEXT NOT NULL \n"
                + ");";
        try {
        	this.connect(); 
        	Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "id TEXT PRIMARY KEY, \n"
                + "username TEXT NOT NULL UNIQUE, \n"
                + "password TEXT \n"
                + ");";
        try {
        	this.connect();
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	public static void main(String[] args) {
		DBManager.getInstance();
		System.out.println("fin");
	}
}
