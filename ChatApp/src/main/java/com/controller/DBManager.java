package main.java.com.controller;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class DBManager {
	private static final DBManager DBmanager = new DBManager();
	private String url = "jdbc:sqlite:chatApp.db";
	private Connection conn;

	private DBManager() {
		super();
		this.connect();
		this.initTables();
	}
	
	public static DBManager getInstance() {
		return DBmanager;
	}
	
	/**
	 * Connects to the local database
	 */
	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn =  DriverManager.getConnection(this.url);			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the necessary tables if not already in the database
	 */
	private void initTables () {
        String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
                + " content BLOB NOT NULL, \n"
                + " time TEXT NOT NULL, \n"
                + " fromId TEXT NOT NULL, \n"
                + " toId TEXT NOT NULL, \n"
                + " PRIMARY KEY (fromId, toId, time) \n"
                + ");";
        try { 
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
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Inserts a given user in the local database
     * 
     * @param id The id of the user we are adding
     * @param username The username of the user we are adding
     */
    public void insertUser(String id, String username) {
        String sql = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, null);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
        		// TODO: user is already in db!
        	}
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * @param id The id of the local user
     * @param username The username of the local user
     * @param hashedPassword A hash of the local user's password
     */
    public void insertThisUser(String id, String username, String hashedPassword) {
        String sql = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
        		// TODO: user is already in db!
        	}
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * @param id The id of the user we want to update
     * @param newUsername The new username of the user
     */
    public void updateUsername(String id, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE id = ?;";

        try {
        	PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newUsername);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
        		// TODO: username is already taken!
        		// check that only when changing your own username?
        	}
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Inserts a message in the messages table
     * 
     * @param content A <code>Blob</code> object that contains the content of the message
     * @param time A string that represents the time of the message
     * @param fromId The user id of the sender
     * @param toId The user id of the receiver
     */
    public void insertMessage(Blob content, String time, String fromId, String toId) {
    	String sql = "INSERT INTO messages(content, time, fromId, toId) VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBlob(1, content);
            pstmt.setString(2, time);
            pstmt.setString(3, fromId);
            pstmt.setString(4, toId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Finds the username corresponding to the given id
     * 
     * @param id The id of the user
     * @return The username associated to the user id
     */
    public String getUsernameFromId(String id) {
    	String sql = "SELECT username FROM users WHERE id = ?;";
    	
    	try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("username");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return "";
    }
    
    /**
     * Returns the hashed password of the local user
     * 
     * @param id The id of the local user
     * @return The hashed password stored in the local database
     * @throws SQLException
     */
    public String getHashedPassword(String id) throws SQLException {
    	String sql = "SELECT password FROM users WHERE id = ?;";
    	PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        return rs.getString("password");
    }
	
    // Used for tests
	public static void main(String[] args) {
		DBManager.getInstance().insertUser("id", "sarah");
		System.out.println(DBManager.getInstance().getUsernameFromId("id"));
		System.out.println("fin");
	}
}
