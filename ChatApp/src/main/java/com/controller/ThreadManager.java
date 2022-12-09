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
public class ThreadManager implements ChatListener {
	// The ThreadManager is a singleton
	private static ThreadManager threadManager = null;
	// Mapping of user's running conversations
	private Map<User, ConversationThread> conversationsMap;
	
	private ThreadManager() {
		conversationsMap = new HashMap<User, ConversationThread>();
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
		conversationsMap.put(user, conversation);
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
	public void onChatClosure(User user) {
		conversationsMap.remove(user);
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
