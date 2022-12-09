/**
 * The ThreadManager manages all active conversations for all online users.
 */

package main.java.com.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.model.ConversationThread;
import main.java.com.model.Message;
import main.java.com.model.TCPServer;
import main.java.com.model.UDPServer;
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
	// Mapping of online users and their running conversations
	private static Map<User, ArrayList<ConversationThread>> conversationsMap;
	
	private ThreadManager() {
		conversationsMap = new HashMap<User, ArrayList<ConversationThread>>();
	}
	
	/**
	 * 
	 * @return the ThreadManager singleton
	 */
	public static ThreadManager getInstance() {
		if (threadManager == null) threadManager = new ThreadManager();
		return threadManager;
	}
	
	public void addConversation(User user, ConversationThread conversation) {
		conversationsMap.get(user).add(conversation);
	}
	
	@Override
	public void onLogin(User user) {
		conversationsMap.put(user, new ArrayList<ConversationThread>());
	}

	@Override
	public void onLogout(User user) {
		conversationsMap.remove(user);
	}

	@Override
	public void onChatRequest(User user) {
		try {
			// Step 1: redirect from UDP to TCP
			DatagramSocket UDPserver = new DatagramSocket();
			DatagramPacket packetInit = new DatagramPacket(Integer.toString(TCPServer.getTCPserverPort()).getBytes(), 4, user.getUserIP(), UDPServer.getUDPserverPort());
			UDPserver.send(packetInit);
			UDPserver.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChatClosure(User user1, User user2) {
		for (ConversationThread conversation: conversationsMap.get(user1)) {
			if (conversation.getRemoteUser() == user2) {
				conversation.close();
				conversationsMap.get(user2).remove(conversation);
				break;
			}
		}
		for (ConversationThread conversation: conversationsMap.get(user2)) {
			if (conversation.getRemoteUser() == user1) {
				conversation.close();
				conversationsMap.get(user1).remove(conversation);
				break;
			}
		}
	}

	@Override
	public void onMessageToSend(User user, Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageToReceive(User user, Message message) {
		// TODO Auto-generated method stub
		
	}
	
}
