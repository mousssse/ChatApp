package main.java.com.controller;

import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.User;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class ListenerManager {
	
	private static ListenerManager listenerManager = null;
	
	private List<ChatListener> chatListeners;
	private List<LoginListener> loginListeners;
	private List<UsernameListener> usernameListeners;
	
	private ListenerManager() {
		chatListeners = new ArrayList<>();
		loginListeners = new ArrayList<>();
		usernameListeners = new ArrayList<>();
	}
	
	public static ListenerManager getInstance() {
		if (listenerManager == null) listenerManager = new ListenerManager();
		return listenerManager;
	}
	
	
	public void addChatListener(ChatListener chatListener) {
		chatListeners.add(chatListener);
	}
	
	public void addLoginListener(LoginListener loginListener) {
		loginListeners.add(loginListener);
	}
	
	public void addUsernameListener(UsernameListener usernameListener) {
		usernameListeners.add(usernameListener);
	}
	
	public void fireOnChatRequest(User user) {
		chatListeners.forEach(chatListener -> chatListener.onChatRequest(user));
	}
	
	public void fireOnChatClosure(User user1, User user2) {
		chatListeners.forEach(chatListener -> chatListener.onChatClosure(user1, user2));
	}
	
	public void fireOnMessageToSend(User user, Message message) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToSend(user, message));
	}
	
	public void fireOnMessageToReceive(User user, Message message) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToReceive(user, message));
	}
	
	public void fireOnLogin(User user) {
		loginListeners.forEach(loginListener -> loginListener.onLogin(user));
	}
	
	public void fireOnLogout(User user) {
		loginListeners.forEach(loginListener -> loginListener.onLogout(user));
	}
	
	public void fireOnUsernameModification(User user, String newUsername) {
		usernameListeners.forEach(usernameListener -> usernameListener.onUsernameModification(user, newUsername));
	}

}
