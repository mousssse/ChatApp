package main.java.com.controller.listener;

import java.net.InetAddress;

import main.java.com.model.User;

public interface LoginListener {
	
	void onLogin(User remoteUser);
	void onLogout(InetAddress inetAddress);

}
