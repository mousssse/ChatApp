package main.java.com.model;

import java.io.IOException;

import main.java.com.controller.ListenerManager;

/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class ConversationThread implements Runnable {
	private Conversation conversation;
	private User remoteUser;
	
	/**
	 * 
	 * @param conversation is the conversation instance
	 * @param remoteUser is the remote user with which this conversation is occurring
	 */
	public ConversationThread(Conversation conversation, User remoteUser) {
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
					case DELETE_MESSAGE:
						ListenerManager.getInstance().fireOnMessageToDelete(message);
						break;
					case REQUEST_CONVERSATION:
						ListenerManager.getInstance().fireOnChatRequestReceived(this.remoteUser);
						break;
					case END_CONVERSATION:
						ListenerManager.getInstance().fireOnChatClosureReceived(this.remoteUser);
						break;
					case ACCEPT_REQUEST:
						ListenerManager.getInstance().fireOnChatAcceptedRequest(this.remoteUser);
						break;
					case CANCEL_REQUEST:
						ListenerManager.getInstance().fireOnChatCancelledRequest(this.remoteUser);
						break;
					default:
						break;
				}
			} catch (IOException e) {
				// Chat was already closed
				ListenerManager.getInstance().fireOnChatClosureReceived(remoteUser);
				return;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
}
