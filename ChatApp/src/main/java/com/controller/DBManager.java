package main.java.com.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private static final DBManager manager = new DBManager();

	// TODO : concatenate with path of app: "jdbc:sqlite:" + path + "/sqlite/db/chatApp.db"
	public static String url = "jdbc:sqlite:chatApp.db";
	
	private Connection conn;

	private DBManager() {
		super();
	}
	
	public static DBManager getInstance() {
		return manager;
	}
	
	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn =  DriverManager.getConnection(url);			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (this.conn != null) {
				try {
					this.conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void initTables () throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
                + " id int PRIMARY KEY, \n"
                + "    from_ip text, \n"
                + " to_ip text, \n"
                + " date text, \n"
                + "    content text \n"
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
                + "ip text PRIMARY KEY, \n"
                + "pseudo text, \n"
                + "connected bool \n"
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
	
	public static void main(String[] args) throws ClassNotFoundException {
		DBManager.getInstance().initTables();
		System.out.println("fin");
	}
}
