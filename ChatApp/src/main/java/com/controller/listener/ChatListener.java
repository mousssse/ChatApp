package main.java.com.controller.listener;

import main.java.com.model.User;
import main.java.com.model.UserThread;

public interface ChatListener {
	
	// Parameters to be determined tomorrow, I'm just brainstorming

	// What happens if a user requests to chat with another online user?
	void onChatRequestReceived(User user, int port);
	// What happens once the chat is accepted by the other user?
	void onChatRequestSent(User user, int port);
	// What happens when one of both parties decides to end the chat?
	void onChatClosureRequested(User user, UserThread userThread);
	// What happens when a user wants to send a message?
	void onMessageToSend();
	// What happens when a user has to receive a message?
	void onMessageToReceive();

}
