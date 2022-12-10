/**
 * The ThreadManager manages all active conversations for all online users.
 */

package main.java.com.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.model.Conversation;
import main.java.com.model.Message;
import main.java.com.model.User;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class ThreadManager implements ChatListener, LoginListener {
	// The ThreadManager is a singleton
	private static ThreadManager threadManager = null;
	// Mapping of user's running conversations
	private Map<User, Conversation> conversationsMap;
	
	private ThreadManager() {
		conversationsMap = new HashMap<User, Conversation>();
	}
	
	/**
	 * 
	 * @return the ThreadManager singleton
	 */
	public static ThreadManager getInstance() {
		if (threadManager == null) threadManager = new ThreadManager();
		return threadManager;
	}
	
	public void clearConversationsMap() {
		this.conversationsMap.values().forEach(conversation -> conversation.close());
		this.conversationsMap.clear();
	}
	
	public void addConversation(User remoteUser, Conversation conversation) {
		conversationsMap.put(remoteUser, conversation);
	}

	@Override
	public void onChatRequest(User user) {
		try {
			// Step 1: request a TCP connection to the listening server
			System.out.println("Going to connect to the TCP listening server on port");
			Socket socket = new Socket(user.getIP(), user.getTCPserverPort());
			
			// Connecting to the new TCP server
			DataInputStream in = new DataInputStream(socket.getInputStream());
			int newTcpPort = in.readInt();
			socket.close();
			socket = new Socket(user.getIP(), newTcpPort);
			User remoteUser = OnlineUsersManager.getInstance().getUserFromIP(socket.getInetAddress());
			this.addConversation(remoteUser, new Conversation(socket));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChatClosure(User user) {
		conversationsMap.remove(user).close();;
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent) {
		try {
			this.conversationsMap.get(remoteUser).write(localUser.getId(), remoteUser.getId(), messageContent);
		} catch (IOException | SQLException e) {
			// message was not received
		}
		
	}

	@Override
	public void onMessageToReceive(Message message) {
		// Nothing to do i think
	}

	@Override
	public void onLogin(User remoteUser) {
		// Nothing to do
	}

	@Override
	public void onLogout(InetAddress inetAddress) {
		this.conversationsMap.remove(OnlineUsersManager.getInstance().getUserFromIP(inetAddress));
	}
	
}
