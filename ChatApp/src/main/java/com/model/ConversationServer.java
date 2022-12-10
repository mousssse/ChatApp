package main.java.com.model;

import java.io.IOException;

import main.java.com.controller.ListenerManager;

//TODO rename class
public class ConversationServer implements Runnable {
	private Conversation conversation;
	private User remoteUser;
	
	public ConversationServer(Conversation conversation, User remoteUser) {
		this.conversation = conversation;
		this.remoteUser = remoteUser;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Message message = this.conversation.read();
				switch (message.getType()) {
					case MESSAGE: 
						ListenerManager.getInstance().fireOnMessageToReceive(message);
						break;
					case CLOSING_CONVERSATION:
						ListenerManager.getInstance().fireOnChatClosure(this.remoteUser);
					default: break;
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				// user has disconnected?
				e.printStackTrace();
			}
		}
	}
	
}
