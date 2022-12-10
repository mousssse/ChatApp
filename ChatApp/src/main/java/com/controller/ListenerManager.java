package main.java.com.controller;

import java.net.InetAddress;
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
	
	public void fireOnChatRequest(User remoteUser) {
		chatListeners.forEach(chatListener -> chatListener.onChatRequest(remoteUser));
	}
	
	public void fireOnChatClosure(User remoteUser) {
		chatListeners.forEach(chatListener -> chatListener.onChatClosure(remoteUser));
	}
	
	public void fireOnMessageToSend(User localUser, User remoteUser, String messageContent) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToSend(localUser, remoteUser, messageContent));
	}
	
	public void fireOnMessageToReceive(Message message) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToReceive(message));
	}
	
	public void fireOnLogin(User remoteUser) {
		loginListeners.forEach(loginListener -> loginListener.onLogin(remoteUser));
	}
	
	public void fireOnLogout(InetAddress inetAddress) {
		loginListeners.forEach(loginListener -> loginListener.onLogout(inetAddress));
	}
	
	public void fireOnUsernameModification(User user, String newUsername) {
		usernameListeners.forEach(usernameListener -> usernameListener.onUsernameModification(user, newUsername));
	}

}
