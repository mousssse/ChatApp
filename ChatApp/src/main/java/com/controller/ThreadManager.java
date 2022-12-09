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
	private static final ThreadManager threadManager = new ThreadManager();
	// Mapping of online users and their running conversations
	private static Map<User, ArrayList<ConversationThread>> conversationsMap;
	
	private ThreadManager() {
		conversationsMap = new HashMap<User, ArrayList<ConversationThread>>();
	}
	
	public void addConversation(User user, ConversationThread conversation) {
		conversationsMap.get(user).add(conversation);
	}
	
	
	/**
	 * 
	 * @return the ThreadManager singleton
	 */
	public static ThreadManager getInstance() {
		return threadManager;
	}
	
	/**
	 * Adds a user to the ThreadManager's map.
	 * @param user is the user object
	 */
	public void addUser(User user) {
		conversationsMap.put(user, new ArrayList<ConversationThread>());
	}
	
	/**
	 * Removes a user from the ThreadManager's map.
	 * @param user is the user object
	 */
	public void removeUser(User user) {
		conversationsMap.remove(user);
	}

	@Override
	public void onChatRequest(User user) {
		try {
			//envoi demande connexion
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
			if (conversation.getWithUser() == user2) {
				conversation.close();
				conversationsMap.get(user2).remove(conversation);
				break;
			}
		}
		for (ConversationThread conversation: conversationsMap.get(user2)) {
			if (conversation.getWithUser() == user1) {
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
	
//	
//	/**
//	 * TODO THIS NEEDS TO BE CHANGED COMPLETELY TOMORROW, SERVERTHREAD IS BEING CREATED INFINITELY I THINK
//	 * @param clientID is the local ID
//	 * @param remoteID is the remote ID
//	 */
//	public void createThread(String clientID, String serverID, String serverIP) {
//		while (true) {
//			try {
//				ServerThread serverThread = new ServerThread(this.nextAvailablePort++, clientID);
//				this.threadsMap.get(serverID).add(serverThread);
//				serverThread.start();
//				break;
//			} catch (IOException e) {
//				/**
//				 * If an exception is caught, the next iteration will occur with an incremented (and probably available)
//				 * next available port.
//				 */
//			}
//		}
//		
//		try {
//			ClientThread clientThread = new ClientThread(serverID, serverIP);
//			this.threadsMap.get(clientID).add(clientThread);
//			clientThread.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * 
//	 * @param clientID is the ID of the user playing the role of the client
//	 * @param serverID is the ID of the user playing the role of the server
//	 * @throws IOException
//	 */
//	public void destroyThreads(String clientID, String serverID) throws IOException {
//		for (UserThread thread : this.threadsMap.get(clientID)) {
//			if (thread.getReceiverID().equals(serverID)) {
//				((ClientThread) thread).getClientSocket().close();
//				threadsMap.get(clientID).remove(thread);
//				break;
//			}
//		}
//		for (UserThread thread : this.threadsMap.get(serverID)) {
//			if (thread.getReceiverID().equals(clientID)) {
//				((ServerThread) thread).getServerSocket().close();
//				threadsMap.get(serverID).remove(thread);
//				break;
//			}
//		}
//	}
	
	
}
