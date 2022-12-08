package main.java.com.controller;

import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.User;
import main.java.com.model.UserThread;

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
	
	public void fireOnChatRequestReceived(User user, int port) {
		chatListeners.forEach(chatListener -> chatListener.onChatRequestReceived(user, port));
	}
	
	public void fireOnChatRequestSent(User user, int clientPort) {
		chatListeners.forEach(chatListener -> chatListener.onChatRequestSent(user, clientPort));
	}
	
	public void fireOnChatClose(User user, UserThread userThread) {
		chatListeners.forEach(chatListener -> chatListener.onChatClosureRequested(user, userThread));
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
