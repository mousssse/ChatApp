/**
 * The ThreadManager manages all active conversations.
 */


package main.java.com.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.com.model.ClientThread;
import main.java.com.model.User;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class ThreadManager {
	
	public static final ThreadManager threadManager = new ThreadManager();
	private Map<String, ArrayList<ClientThread>> threadsMap;
	public final static int ACCEPT_PORT = 9000;
	
	public ThreadManager() {
		this.threadsMap = new HashMap<String, ArrayList<ClientThread>>();
	}
	
	public void addNewUser(String ID) {
		this.threadsMap.put(ID, new ArrayList<ClientThread>());
	}
	
	/**
	 * 
	 * @param remoteID is the remote ID.
	 * @return next available port that can be assigned to the remote user's socket.
	 */
	private int nextAvailablePort(String remoteID) {
		int port = 1025;
		ArrayList<ClientThread> activeThreads = threadsMap.get(remoteID);
		if (!activeThreads.isEmpty()) {
			// TODO : when a thread gets killed, you can use its port instead of max+1
			port = (int) (activeThreads.get(activeThreads.size() - 1).getLocalSocket().getLocalPort() + 1);
		}
		return port;
	}
	
	/**
	 * 
	 * @param threadName is the thread name
	 * @param localID is the local ID
	 * @param remoteID is the remote ID
	 */
	public void createThread(String threadName, String localID, String remoteID) {
		ClientThread thread = null;
		try {
			ServerSocket servSocket = new ServerSocket(ACCEPT_PORT);
			Socket localSocket = servSocket.accept();
			Socket remoteSocket = new Socket("localhost", nextAvailablePort(remoteID));
			thread = new ClientThread(threadName, servSocket, localSocket, remoteSocket);
			threadsMap.get(localID).add(thread);
			threadsMap.get(remoteID).add(thread);
			thread.run();
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
	public void destroyThread(String threadName, String localID, String remoteID) throws IOException {
		for (ClientThread thread : this.threadsMap.get(localID)) {
			if (thread.getName().equals(threadName)) {
				thread.getLocalSocket().close();
				thread.getRemoteSocket().close();
				threadsMap.get(localID).remove(thread);
				threadsMap.get(remoteID).remove(thread);
				break;
			}
		}
	}
	
	public static void main(String [] args) {
		User sarah = new User();
		User sandro = new User();
		sarah.setId("001");
		sandro.setId("002");
		threadManager.createThread("t1", sarah.getId(), sandro.getId());
		
		
		
	}
}
