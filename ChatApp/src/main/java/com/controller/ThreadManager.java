/**
 * The ThreadManager manages all active conversations.
 */


package main.java.com.controller;

import java.io.IOException;
import java.net.InetAddress;
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
	
	private static final ThreadManager threadManager = new ThreadManager();
	private Map<String, ArrayList<UserThread>> threadsMap;
	private int nextAvailablePort;
	
	
	private ThreadManager() {
		this.threadsMap = new HashMap<String, ArrayList<UserThread>>();
	}
	
	public void addNewUser(String ID) {
		this.threadsMap.put(ID, new ArrayList<UserThread>());
	}
	
	public static ThreadManager getInstance() {
		return threadManager;
	}
	
	/**
	 * 
	 * @param threadName is the thread name
	 * @param localID is the local ID
	 * @param remoteID is the remote ID
	 */
	public void createThread(String threadID, String clientID, String serverID, String serverIP) {
		while (true) {
			try {
				ServerThread serverThread = new ServerThread(threadID, this.nextAvailablePort++, clientID);
				this.threadsMap.get(serverID).add(serverThread);
				serverThread.start();
				break;
			} catch (IOException e) {
				// We don't have to do anything, it will try again with the incremented nextAvailablePort
			}
		}
		
		try {
			ClientThread clientThread = new ClientThread(threadID, serverID, serverIP);
			this.threadsMap.get(clientID).add(clientThread);
			clientThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param threadName is the thread name
	 * @param localID is the local ID
	 * @param remoteID is the remote ID
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
	
	public static void main(String [] args) throws UnknownHostException {
		User sarah = new User();
		User sandro = new User();
		sarah.setId("001");
		sandro.setId("002");
		threadManager.createThread("t1", sarah.getId(), sandro.getId(), sarah.getUserIP());
		
		
		
	}
}
