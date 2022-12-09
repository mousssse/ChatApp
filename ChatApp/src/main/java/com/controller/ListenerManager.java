package main.java.com.controller;

import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.User;

public class ListenerManager {
	
	private static final ListenerManager listenerManager = new ListenerManager();
	
	private List<ChatListener> chatListeners;
	private List<LoginListener> loginListeners;
	private List<UsernameListener> usernameListeners;
	
	public ListenerManager() {
		chatListeners = new ArrayList<>();
		loginListeners = new ArrayList<>();
		usernameListeners = new ArrayList<>();
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
	
	public void fireOnChatRequestSent(User user) {
		chatListeners.forEach(chatListener -> chatListener.onChatRequest(user));
	}
	
	public void fireOnChatClose(User user1, User user2) {
		chatListeners.forEach(chatListener -> chatListener.onChatClosure(user1, user2));
	}
	
	public void fireOnMessageToSend(User user, Message message) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToSend(user, message));
	}
	
	public void fireOnMessageToReceive(User user, Message message) {
		chatListeners.forEach(chatListener -> chatListener.onMessageToReceive(user, message));
	}
	
	public void fireOnLogin() {
		loginListeners.forEach(LoginListener::onLogin);
	}
	
	public void fireOnLogout() {
		loginListeners.forEach(LoginListener::onLogout);
	}
	
	public void fireOnUsernameModification(User user) {
		usernameListeners.forEach(usernameListener -> usernameListener.onUsernameModification(user));
	}

	public static ListenerManager getListenermanager() {
		return listenerManager;
	}
	
	
	
	

}
