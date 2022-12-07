package main.java.com.controller.listener;

public interface ChatListener {
	
	// Parameters to be determined tomorrow, I'm just brainstorming
	
	// What happens if a user requests to chat with another online user?
	void onChatRequest();
	// What happens once the chat is accepted by the other user?
	void onChatActive();
	// What happens when one of both parties decides to end the chat?
	void onChatClose();
	// What happens when a user wants to send a message?
	void onMessageToSend();
	// What happens when a user has to receive a message?
	void onMessageToReceive();

}
