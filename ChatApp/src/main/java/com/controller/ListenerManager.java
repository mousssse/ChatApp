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
		chatListeners.add(chatListener);
	}
	
	/**
	 * 
	 * @param loginListener is the loginListener instance
	 */
	public void addLoginListener(LoginListener loginListener) {
		loginListeners.add(loginListener);
	}
	
	/**
	 * 
	 * @param usernameListener is the usernameListener listener
	 */
	public void addUsernameListener(UsernameListener usernameListener) {
		usernameListeners.add(usernameListener);
	}
	
	/**
	 * 
	 * @param selfLoginListener is the selfLoginListener listener
	 */
	public void addSelfLoginListener(SelfLoginListener selfLoginListener) {
		selfLoginListeners.add(selfLoginListener);
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnChatRequest(User remoteUser) {
		chatListeners.forEach(chatListener -> chatListener.onChatRequest(remoteUser));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnChatClosure(User remoteUser) {
		chatListeners.forEach(chatListener -> chatListener.onChatClosure(remoteUser));
	}
	
	/**
	 * @param localUser The local user sending a message
	 * @param remoteUser The receiver of the message
	 * @param messageContent The content of the message
	 * @param date The time at which the message was sent
	 */
	public void fireOnMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToSend(localUser, remoteUser, messageContent, date));
	}
	
	/**
	 * 
	 * @param message is the message to receive
	 */
	public void fireOnMessageToReceive(Message message) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToReceive(message));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnLogin(User remoteUser) {
		loginListeners.forEach(loginListener -> loginListener.onLogin(remoteUser));
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 */
	public void fireOnLogout(User remoteUser) {
		loginListeners.forEach(loginListener -> loginListener.onLogout(remoteUser));
	}
	
	/**
	 * 
	 * @param id is the local user's id
	 * @param username is the local user's username
	 */
	public void fireOnSelfLogin(String username, String password) {
		dbListeners.forEach(dbListener -> dbListener.onSelfLogin(username, password));
	}
	
	public void fireOnSelfLoginNext(String username) {
		selfLoginListeners.forEach(selfLoginListener -> selfLoginListener.onSelfLoginOnlineUsers(username));
		selfLoginListeners.forEach(SelfLoginListener::onSelfLoginNetwork);
	}
	
	public void fireOnSelfLogout() {
		selfLoginListeners.forEach(selfLoginListener -> selfLoginListener.onSelfLogout());
	}
	
	/**
	 * 
	 * @param user is the remote user
	 * @param newUsername is remote user's new username
	 */
	public void fireOnUsernameModification(User user, String newUsername) {
		usernameListeners.forEach(usernameListener -> usernameListener.onUsernameModification(user, newUsername));
	}
	
	/**
	 * 
	 * @param newUsername is the local user's new username
	 */
	public void fireOnSelfUsernameModification(String newUsername) {
		usernameListeners.forEach(usernameListener -> usernameListener.onSelfUsernameModification(newUsername));
	}

}
