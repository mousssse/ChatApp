package main.java.com.controller.listener;

import main.java.com.model.User;

public interface UsernameListener {
	
	void onUsernameModification(User user, String newUsername);

}
