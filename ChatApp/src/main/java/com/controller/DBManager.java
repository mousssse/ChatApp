package main.java.com.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.sqlite.SQLiteException;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.DBListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * The DBManager manages the local user's database.
 * @author sarah
 * @author Sandro
 *
 */
public class DBManager implements DBListener, LoginListener, ChatListener {
	private static DBManager dbManager = null;
	private String url = "jdbc:sqlite:chatApp.db";
	private Connection conn;

	private DBManager() {
		super();
		this.connect();
		this.initTables();
	}
	
	/**
	 * 
	 * @return the DBManager singleton.
	 */
	public static DBManager getInstance() {
		if (dbManager == null) dbManager = new DBManager();
		return dbManager;
	}
	
	/**
	 * Connects to the local database
	 */
	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn = DriverManager.getConnection(this.url);			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the necessary tables if not already in the database
	 */
	private void initTables () {
        String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
                + " content TEXT NOT NULL, \n"
                + " time TEXT NOT NULL, \n"
                + " fromId TEXT NOT NULL, \n"
                + " toId TEXT NOT NULL, \n"
                + " PRIMARY KEY (fromId, toId, time) \n"
                + ");";
        try { 
        	Statement stmt = this.conn.createStatement();
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
            Statement stmt = this.conn.createStatement();
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Inserts a given user in the local database
     * 
     * @param id is the id of the user we are adding
     * @param username is the username of the user we are adding
     */
    public void insertUser(String username, String id) {
        String sql = "INSERT OR IGNORE INTO users(id, username, password) VALUES(?, ?, ?)";

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
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
     * The database will only be updated when the user first logs in
     * 
     * @param username is the username of the local user
     * @param hashedPassword is a hash of the local user's password
     */
    public void insertThisUser(String username, String hashedPassword) {
    	String sqlCheckExisting = "SELECT * FROM users WHERE password IS NOT NULL;";
    	
    	try {
    		Statement stmt = this.conn.createStatement();
    		ResultSet rs = stmt.executeQuery(sqlCheckExisting);
            if (rs.isBeforeFirst()) {
    			// local user already exists in the database, no need to insert them
            	return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	
    	// On first login
        String sql = "INSERT OR IGNORE INTO users(id, username, password) VALUES(?, ?, ?)";
        String id = UUID.randomUUID().toString();

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * @param id is the id of the user we want to update
     * @param newUsername is the new username of the user
     */
    public void updateUsername(String id, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE id = ?;";

        try {
        	PreparedStatement pstmt = this.conn.prepareStatement(sql);
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
    public void insertMessage(String content, String time, String fromId, String toId) {
    	if (content.equals("")) return;
    	String sql = "INSERT INTO messages(content, time, fromId, toId) VALUES(?, ?, ?, ?);";
    	
        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, content);
            pstmt.setString(2, time);
            pstmt.setString(3, fromId);
            pstmt.setString(4, toId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Gets the messages of a conversation with a remote user
     * 
     * @param remoteUserId The user id with whom we want to check our messages
     * @return A ResultSet of the messages database rows that correspond
     * @throws SQLException - SQLException
     */
    public List<Message> getConversationHistory(String remoteUserId) throws SQLException {
    	String sql = "SELECT * FROM messages WHERE toId = ? OR fromId = ? ORDER BY time ASC;";
    	
		PreparedStatement pstmt = this.conn.prepareStatement(sql);
	    pstmt.setString(1, remoteUserId);
	    pstmt.setString(2, remoteUserId);
	    ResultSet res = pstmt.executeQuery();
	    
	    List<Message> history = new ArrayList<Message>();
	    while (res.next()) {
	    	String content = res.getString("content");
	    	String timeString = res.getString("time");
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    	LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
	    	String fromId = res.getString("fromId");
	    	User fromUser = null, toUser = null;
	    	if (fromId.equals(remoteUserId)) {
	    		fromUser = OnlineUsersManager.getInstance().getUserFromID(remoteUserId);
	    		toUser = OnlineUsersManager.getInstance().getLocalUser();
	    	}
	    	else {
	    		fromUser = OnlineUsersManager.getInstance().getLocalUser();
	    		toUser = OnlineUsersManager.getInstance().getUserFromID(remoteUserId);
	    	}
	    	Message message = new Message(fromUser, toUser, content, dateTime, MessageType.MESSAGE);
	    	history.add(message);
	    }
	    return history;
    }
    
    /**
     * Finds the username corresponding to the given id
     * 
     * @param id is the id of the user
     * @return the username associated with the user id
     */
    public String getUsernameFromId(String id) {
    	String sql = "SELECT username FROM users WHERE id = ?;";
    	
    	try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("username");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return "";
    }
    
    /**
     * Finds the id corresponding to the given username
     * 
     * @param username is the username of the user
     * @return the id associated with the username
     */
    public String getIdFromUsername(String username) {
    	String sql = "SELECT id FROM users WHERE username = ?;";
    	
    	try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return "";
    }
    
    /**
     * Returns the hashed password of the local user
     * 
     * @return the hashed password stored in the local database
     * @throws SQLException - SQLException
     */
    public String getHashedPassword() throws SQLException {
    	String sql = "SELECT password FROM users WHERE password IS NOT NULL;";
    	Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
        return rs.getString("password");
    }
    
    public String getLocalUsername() throws SQLException {
    	String sql = "SELECT username FROM users WHERE password IS NOT NULL;";
    	Statement stmt = this.conn.createStatement();
    	try {
    		ResultSet rs = stmt.executeQuery(sql);
    		return rs.getString("username");
    	} catch (SQLiteException e) {
    		// The db doesn't contain the local user yet,
    		// this is the first time the user opens the app
    		return null;
    	}
    }
    
    /**
     * 
     * @return all usernames in the database
     */
    public List<String> getAllUsernames() {
    	String sql = "SELECT username FROM users WHERE password IS NULL;";
    	List<String> usernamesList = new ArrayList<String>();
    	try {
    		Statement stmt = this.conn.createStatement();
    		ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
    			usernamesList.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return usernamesList;
    }

	@Override
	public void onSelfLogin(String username, String password) {
		this.insertThisUser(username, password);
	}

	@Override
	public void onLogin(User remoteUser) {
		this.insertUser(remoteUser.getUsername(), remoteUser.getId());
		
	}

	@Override
	public void onLogout(User remoteUser) {
		// Nothing to do
		
	}
	@Override
	public void onChatRequest(User remoteUser) {
		// Nothing to do
		
	}

	@Override
	public void onChatClosure(User remoteUser) {
		// Nothing to do
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date) {
		// Nothing to do
	}
	
	@Override
	public void onMessageSuccessfullySent(User localUser, User remoteUser, String messageContent, LocalDateTime date) {
		this.insertMessage(messageContent, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), localUser.getId(), remoteUser.getId());
	}

	@Override
	public void onMessageToReceive(Message message) {
		this.insertMessage(message.getContent(), message.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), message.getFromUser().getId(), message.getToUser().getId());
	}
}
