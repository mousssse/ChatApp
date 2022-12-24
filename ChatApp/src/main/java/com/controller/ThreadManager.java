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
	public void onChatRequest(User user) {
		try {
			// Step 1: request a TCP connection to the listening server
			System.out.println("Going to connect to the TCP listening server on port");
			Socket socket = new Socket(user.getIP(), user.getTCPserverPort());
			
			// Step 2: Connecting to the redirected TCP server
			DataInputStream in = new DataInputStream(socket.getInputStream());
			int newTcpPort = in.readInt();
			socket.close();
			socket = new Socket(user.getIP(), newTcpPort);
			User remoteUser = OnlineUsersManager.getInstance().getUserFromIP(socket.getInetAddress());
			Conversation conversation = new Conversation(socket);
			this.addConversation(remoteUser, conversation);
			new Thread(new ConversationThread(conversation, remoteUser), "Conversation with " + remoteUser.getUsername()).start();
			System.out.println("Conversation with " + remoteUser.getUsername() + " launched.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChatClosure(User user) {
		// Close the conversation socket and remove the conversation from the map
		this.conversationsMap.remove(user).close();
		System.out.println("Conversation with " + user.getUsername() + " closed.");
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date) {
		try {
			this.conversationsMap.get(remoteUser).write(localUser, remoteUser, messageContent, date);
			ListenerManager.getInstance().fireOnMessageSuccessfullySent(localUser, remoteUser, messageContent, date);
		} catch (IOException | SQLException e) {
			// Message was not received because chat has been closed
			System.out.println("Chat was already closed.");
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
		this.conversationsMap.remove(remoteUser).close();
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
