package main.java.com.controller.listener;

import main.java.com.model.User;

public interface LoginListener {
	
	void onLogin(User user);
	void onLogout(User user);

}
