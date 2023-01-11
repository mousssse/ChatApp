package main.java.com.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.DBListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.SelfLoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * The ListenerManager manages all listeners.
 * @author Sandro
 * @author sarah
 *
 */
public class ListenerManager {
	
	private static ListenerManager listenerManager = null;
	
	private List<ChatListener> chatListeners;
	private List<LoginListener> loginListeners;
	private List<SelfLoginListener> selfLoginListeners;
	private List<DBListener> dbListeners;
	private List<UsernameListener> usernameListeners;
	
	private ListenerManager() {
		this.chatListeners = new ArrayList<>();
		this.loginListeners = new ArrayList<>();
		this.selfLoginListeners = new ArrayList<>();
		this.usernameListeners = new ArrayList<>();
		this.dbListeners = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return the ListenerManager singleton
	 */
	public static ListenerManager getInstance() {
		if (listenerManager == null) listenerManager = new ListenerManager();
		return listenerManager;
	}
	
	/**
	 * 
	 * @param chatListener is the chatListener instance
	 */
	public void addChatListener(ChatListener chatListener) {
		this.chatListeners.add(chatListener);
	}
	
	/**
	 * 
	 * @param loginListener is the loginListener instance
	 */
	public void addLoginListener(LoginListener loginListener) {
		this.loginListeners.add(loginListener);
	}
	
	/**
	 * 
	 * @param usernameListener is the usernameListener listener
	 */
	public void addUsernameListener(UsernameListener usernameListener) {
		this.usernameListeners.add(usernameListener);
	}
	
	/**
	 * 
	 * @param selfLoginListener is the selfLoginListener listener
	 */
	public void addSelfLoginListener(SelfLoginListener selfLoginListener) {
		this.selfLoginListeners.add(selfLoginListener);
	}
	
	/**
	 * 
	 * @param dbListener is the DBListener listener
	 */
	public void addDBListener(DBListener dbListener) {
		this.dbListeners.add(dbListener);
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnChatRequestReceived(User remoteUser) {
		this.chatListeners.forEach(chatListener -> chatListener.onChatRequestReceived(remoteUser));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnChatRequest(User remoteUser) {
		this.chatListeners.forEach(chatListener -> chatListener.onChatRequest(remoteUser));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnChatClosureReceived(User remoteUser) {
		this.chatListeners.forEach(chatListener -> chatListener.onChatClosureReceived(remoteUser));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnChatClosure(User remoteUser) {
		this.chatListeners.forEach(chatListener -> chatListener.onChatClosure(remoteUser));
	}
	
	/**
	 * @param localUser is the user sending a message
	 * @param remoteUser is the receiver of the message
	 * @param messageContent is the content of the message
	 * @param date is the date and time at which the message was sent
	 */
	public void fireOnMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date, MessageType type) {
		this.chatListeners.forEach(chatListener -> chatListener.onMessageToSend(localUser, remoteUser, messageContent, date, type));
	}
	
	/**
	 * @param localUser is the user sending a message
	 * @param remoteUser is the receiver of the message
	 * @param messageContent is the content of the message
	 * @param date is the date and time at which the message was sent
	 */
	public void fireOnMessageSuccessfullySent(User localUser, User remoteUser, String messageContent, LocalDateTime date, MessageType type) {
		this.dbListeners.forEach(dbListener -> dbListener.onMessageSuccessfullySent(localUser, remoteUser, messageContent, date, type));
	}
	
	/**
	 * 
	 * @param message is the message to receive
	 */
	public void fireOnMessageToReceive(Message message) {
		this.chatListeners.forEach(chatListener -> chatListener.onMessageToReceive(message));
		this.dbListeners.forEach(dbListener -> dbListener.onMessageToReceiveDB(message));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnLogin(User remoteUser) {
		this.loginListeners.forEach(loginListener -> loginListener.onLogin(remoteUser));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnLogout(User remoteUser) {
		this.loginListeners.forEach(loginListener -> loginListener.onLogout(remoteUser));
	}
	
	/**
	 * When the local user logs in, DB self login listeners will be fired.
	 * @param username is the local user's username
	 * @param password is the local user's password
	 */
	public void fireOnSelfLoginDB(String username, String password) {
		this.dbListeners.forEach(dbListener -> dbListener.onSelfLoginDB(username, password));
	}
	
	/**
	 * When the local user logs in, OnlineUsersManager self login listeners will be fired.
	 * @param username is the local user's username
	 */
	public void fireOnSelfLoginOnline(String username) {
		this.selfLoginListeners.forEach(selfLoginListener -> selfLoginListener.onSelfLoginOnlineUsers(username));
	}
	
	/**
	 * When the local user logs in, NetworkManager self login listeners will be fired.
	 */
	public void fireOnSelfLoginNetwork() {
		this.selfLoginListeners.forEach(SelfLoginListener::onSelfLoginNetwork);
	}
	
	public void fireOnSelfLogout() {
		this.selfLoginListeners.forEach(selfLoginListener -> selfLoginListener.onSelfLogout());
	}
	
	/**
	 * 
	 * @param user is the remote user
	 * @param newUsername is remote user's new username
	 */
	public void fireOnUsernameModification(User user, String newUsername) {
		this.usernameListeners.forEach(usernameListener -> usernameListener.onUsernameModification(user, newUsername));
	}
	
	/**
	 * 
	 * @param newUsername is the local user's new username
	 */
	public void fireOnSelfUsernameModification(String newUsername) {
		this.usernameListeners.forEach(usernameListener -> usernameListener.onSelfUsernameModification(newUsername));
	}

}
