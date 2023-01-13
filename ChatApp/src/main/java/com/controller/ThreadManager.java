package main.java.com.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.SelfLoginListener;
import main.java.com.model.Conversation;
import main.java.com.model.ConversationThread;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * The ThreadManager manages all active conversations for all online users.
 * @author Sandro
 * @author sarah
 *
 */
public class ThreadManager implements ChatListener, LoginListener, SelfLoginListener {

	private static ThreadManager threadManager = null;
	private Map<User, Conversation> conversationsMap;
	
	private ThreadManager() {
		ListenerManager.getInstance().addChatListener(this);
		ListenerManager.getInstance().addLoginListener(this);
		ListenerManager.getInstance().addSelfLoginListener(this);
		this.conversationsMap = new HashMap<User, Conversation>();
	}
	
	/**
	 * 
	 * @return the ThreadManager singleton
	 */
	public static ThreadManager getInstance() {
		if (threadManager == null) threadManager = new ThreadManager();
		return threadManager;
	}
	
	/**
	 * 
	 * @param remoteUser is the remote user
	 * @param conversation is the conversation with the remote user
	 */
	public void addConversation(User remoteUser, Conversation conversation) {
		this.conversationsMap.put(remoteUser, conversation);
	}
	
	@Override
	public void onChatRequestReceived(User remoteUser) {
		// Nothing to do 
		// TODO: think more about this
	}

	@Override
	public void onChatRequest(User remoteUser) {
		try {
			// Step 1: request a TCP connection to the listening server
			System.out.println("Going to connect to the TCP listening server on port");
			Socket socket = new Socket(remoteUser.getIP(), remoteUser.getTCPserverPort());
			
			// Step 2: Connecting to the redirected TCP server
			DataInputStream in = new DataInputStream(socket.getInputStream());
			int newTcpPort = in.readInt();
			socket.close();
			socket = new Socket(remoteUser.getIP(), newTcpPort);
			
			// Step 3: creating a conversation
			Conversation conversation = new Conversation(socket);
			this.addConversation(remoteUser, conversation);
			new Thread(new ConversationThread(conversation, remoteUser), "Conversation with " + remoteUser.getUsername()).start();
			System.out.println("Conversation with " + remoteUser.getUsername() + " requested.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onChatClosureReceived(User remoteUser) {
		// Close the conversation socket and remove the conversation from the map
		if (this.conversationsMap.get(remoteUser) != null) {
			this.conversationsMap.remove(remoteUser).close();
			System.out.println("Conversation with " + remoteUser.getUsername() + " requested to end.");
		}
	}

	@Override
	public void onChatClosure(User remoteUser) {
		// Close the conversation socket and remove the conversation from the map
		if (this.conversationsMap.get(remoteUser) != null) {
			this.conversationsMap.remove(remoteUser).close();
			System.out.println("Conversation with " + remoteUser.getUsername() + " ended.");
		}
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date, MessageType type) {
		try {
			this.conversationsMap.get(remoteUser).write(localUser, remoteUser, messageContent, date, type);
			ListenerManager.getInstance().fireOnMessageSuccessfullySent(localUser, remoteUser, messageContent, date, type);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessageToReceive(Message message) {
		System.out.println(message.getContent());
	}

	@Override
	public void onLogin(User remoteUser) {
		// Nothing to do
	}

	@Override
	public void onLogout(User remoteUser) {
		if (this.conversationsMap.get(remoteUser) != null) this.conversationsMap.remove(remoteUser).close();
	}

	@Override
	public void onSelfLoginOnlineUsers(String username) {
		// Nothing to do
	}
	
	@Override
	public void onSelfLoginNetwork() {
		// Nothing to do
	}

	@Override
	public void onSelfLogout() {
		// Close all conversations
		this.conversationsMap.values().forEach(conversation -> conversation.close());
		this.conversationsMap.clear();
	}
	
}
