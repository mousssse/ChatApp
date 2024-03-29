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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import main.java.com.controller.listener.DBListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * The DBManager manages the local user's database.
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class DBManager implements DBListener, LoginListener {
	private static DBManager dbManager = null;
	private String url = "jdbc:sqlite:chatApp.db";
	private Connection conn;
	public final static String deletedMessage = "This message was deleted";

	private DBManager() {
		super();
		ListenerManager.getInstance().addDBListener(this);
		ListenerManager.getInstance().addLoginListener(this);
		this.connect();
		this.initTables();
	}

	/**
	 * 
	 * @return the DBManager singleton.
	 */
	public static DBManager getInstance() {
		if (dbManager == null)
			dbManager = new DBManager();
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
	private void initTables() {
		String sql = "CREATE TABLE IF NOT EXISTS messages (\n" + " content TEXT NOT NULL, \n"
				+ " time TEXT NOT NULL, \n" + " fromId TEXT NOT NULL, \n" + " toId TEXT NOT NULL, \n"
				+ " PRIMARY KEY (fromId, toId, time) \n" + ");";
		try {
			Statement stmt = this.conn.createStatement();
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		sql = "CREATE TABLE IF NOT EXISTS users (\n" + "id TEXT PRIMARY KEY, \n" + "username TEXT NOT NULL UNIQUE, \n"
				+ "password TEXT \n" + ");";
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
	 * @param id       is the id of the user we are adding
	 * @param username is the username of the user we are adding
	 */
	private void insertUser(String username, String id) {
		String sql = "INSERT OR IGNORE INTO users(id, username, password) VALUES(?, ?, ?)";

		try {
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, username);
			pstmt.setString(3, null);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Checks if the local user already exists in the database
	 * 
	 * @return true if the user is in the database, false otherwise
	 */
	public boolean localUserIsInDB() {
		String sqlCheckExisting = "SELECT * FROM users WHERE password IS NOT NULL;";

		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCheckExisting);
			if (rs.isBeforeFirst()) {
				// local user already exists in the database
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * The database will only be updated when the user first logs in
	 * 
	 * @param username       is the username of the local user
	 * @param hashedPassword is a hash of the local user's password
	 */
	private void insertThisUser(String username, String hashedPassword) {
		if (!this.localUserIsInDB()) {
			// On first login only
			String sql = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";
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
	}

	/**
	 * @param id          is the id of the user we want to update
	 * @param newUsername is the new username of the user
	 * 
	 * @return a boolean that's true if the username isn't already taken and has
	 *         been updated in the database, false otherwise
	 */
	public boolean updateUsername(String id, String newUsername) {
		String sql = "UPDATE users SET username = ? WHERE id = ?;";

		try {
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setString(1, newUsername);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
				// TODO: username is already taken!
				// check that only when changing your own username?
				return false;
			}
			// This shouldn't happen
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * Inserts a message in the messages table
	 * 
	 * @param content A string that contains the content of the message
	 * @param time    A string that represents the time of the message
	 * @param fromId  The user id of the sender
	 * @param toId    The user id of the receiver
	 * @param type    The message type
	 */
	private void insertMessage(String content, String time, String fromId, String toId) {
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
	 * Removes a message from the messages table
	 * 
	 * @param time   The time of the message we want to remove
	 * @param fromId The id of the sender
	 * @param toId   The id of the receiver
	 */
	private void removeMessage(Message message) {
		String time = message.getDate().format(Message.formatter);
		String fromId = message.getFromUser().getId();
		String toId = message.getToUser().getId();

		String sql = "UPDATE messages SET content = ? WHERE time = ? AND fromId = ? AND toId = ?;";

		try {
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setString(1, DBManager.deletedMessage);
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
	 * @return A ResultSet of the messages database rows that correspond to the
	 *         conversation
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
			LocalDateTime dateTime = LocalDateTime.parse(timeString, Message.formatter);
			String fromId = res.getString("fromId");
			User fromUser = null, toUser = null;
			if (fromId.equals(remoteUserId)) {
				fromUser = OnlineUsersManager.getInstance().getUserFromID(remoteUserId);
				toUser = OnlineUsersManager.getInstance().getLocalUser();
			} else {
				fromUser = OnlineUsersManager.getInstance().getLocalUser();
				toUser = OnlineUsersManager.getInstance().getUserFromID(remoteUserId);
			}

			// This is for when we are looking at a conversation with an offline user
			if (fromUser == null)
				fromUser = new User(remoteUserId, this.getUsernameFromId(remoteUserId), null, 0);
			if (toUser == null)
				toUser = new User(remoteUserId, this.getUsernameFromId(remoteUserId), null, 0);

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
			return null;
		}
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
			return null;
		}
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

	/**
	 * Gets the username of the local user in the database
	 * 
	 * @return a string that is the username of the local user
	 */
	public String getLocalUsername() {
		String sql = "SELECT username FROM users WHERE password IS NOT NULL;";
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs.getString("username");
		} catch (SQLException e) {
			// The db doesn't contain the local user yet,
			// this is the first time the user opens the app.
			// This shouldn't happen
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Gets the map of all known users' ids and usernames
	 * 
	 * @return a map of all usernames mapped to their id in the database
	 */
	public Map<String, String> getAllUsernames() {
		String sql = "SELECT username, id FROM users WHERE password IS NULL;";
		Map<String, String> usernameMap = new HashMap<String, String>();
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				usernameMap.put(rs.getString("id"), rs.getString("username"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return usernameMap;
	}

	@Override
	public void onSelfLoginDB(String username, String password) {
		this.insertThisUser(username, password);
	}

	@Override
	public void onLogin(User remoteUser) {
		if (this.getAllUsernames().keySet().contains(remoteUser.getId())) {
			this.updateUsername(remoteUser.getId(), remoteUser.getUsername());
		} else {
			this.insertUser(remoteUser.getUsername(), remoteUser.getId());
		}
	}

	@Override
	public void onLogout(User remoteUser) {
		// Nothing to do

	}

	@Override
	public void onMessageSuccessfullySent(User localUser, User remoteUser, String messageContent, LocalDateTime date) {
		this.insertMessage(messageContent, date.format(Message.formatter), localUser.getId(), remoteUser.getId());
	}

	@Override
	public void onMessageToReceiveDB(Message message) {
		this.insertMessage(message.getContent(),
				message.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				message.getFromUser().getId(), message.getToUser().getId());
	}

	@Override
	public void onMessageToDeleteDB(Message message) {
		this.removeMessage(message);
	}
}
