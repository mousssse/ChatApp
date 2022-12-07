/**
 * The ThreadManager manages all active conversations for all online users.
 */

package main.java.com.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.com.model.ClientThread;
import main.java.com.model.ServerThread;
import main.java.com.model.User;
import main.java.com.model.UserThread;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class ThreadManager {
	// The ThreadManager is a singleton
	private static final ThreadManager threadManager = new ThreadManager();
	// Mapping of online users and their running conversations
	private Map<String, ArrayList<UserThread>> threadsMap;
	// Next available port of the server socket (works by increments of 1)
	private int nextAvailablePort;
	
	private ThreadManager() {
		this.threadsMap = new HashMap<String, ArrayList<UserThread>>();
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
	 * @param userID is the user to add's ID.
	 */
	public void addUser(String userID) {
		this.threadsMap.put(userID, new ArrayList<UserThread>());
	}
	
	/**
	 * 
	 * @param userID is the user to remove's ID.
	 */
	public void removeUser(String userID) {
		this.threadsMap.remove(userID);
	}
	
	/**
	 * TODO THIS NEEDS TO BE CHANGED COMPLETELY TOMORROW, SERVERTHREAD IS BEING CREATED INFINITELY I THINK
	 * @param clientID is the local ID
	 * @param remoteID is the remote ID
	 */
	public void createThread(String clientID, String serverID, String serverIP) {
		while (true) {
			try {
				ServerThread serverThread = new ServerThread(this.nextAvailablePort++, clientID);
				this.threadsMap.get(serverID).add(serverThread);
				serverThread.start();
				break;
			} catch (IOException e) {
				/**
				 * If an exception is caught, the next iteration will occur with an incremented (and probably available)
				 * next available port.
				 */
			}
		}
		
		try {
			ClientThread clientThread = new ClientThread(serverID, serverIP);
			this.threadsMap.get(clientID).add(clientThread);
			clientThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param clientID is the ID of the user playing the role of the client
	 * @param serverID is the ID of the user playing the role of the server
	 * @throws IOException
	 */
	public void destroyThreads(String clientID, String serverID) throws IOException {
		for (UserThread thread : this.threadsMap.get(clientID)) {
			if (thread.getReceiverID().equals(serverID)) {
				((ClientThread) thread).getClientSocket().close();
				threadsMap.get(clientID).remove(thread);
				break;
			}
		}
		for (UserThread thread : this.threadsMap.get(serverID)) {
			if (thread.getReceiverID().equals(clientID)) {
				((ServerThread) thread).getServerSocket().close();
				threadsMap.get(serverID).remove(thread);
				break;
			}
		}
	}
	
//	public static void main(String [] args) throws UnknownHostException {
//		User sarah = new User();
//		User sandro = new User();
//		sarah.setId("001");
//		sandro.setId("002");
//		threadManager.createThread(sarah.getId(), sandro.getId(), sarah.getUserIP());
//	}
}
