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

/**
 * 
 * @author Sandro
 * @author Sarah
 *
 */
public class ThreadManager {
	
	private Map<String, ArrayList<ClientThread>> threadsMap = new HashMap<String, ArrayList<ClientThread>>();
	public final static int ACCEPT_PORT = 9000;
	
	/**
	 * 
	 * @param remoteUsername is the remote username.
	 * @return next available port that can be assigned to the remote user's socket.
	 */
	private int nextAvailablePort(String remoteUsername) {
		int port = 1025;
		ArrayList<ClientThread> activeThreads = threadsMap.get(remoteUsername);
		if (!activeThreads.isEmpty()) {
			// TODO : when a thread gets killed, you can use its port instead of max+1
			port = (int) (activeThreads.get(activeThreads.size() - 1).getLocalSocket().getLocalPort() + 1);
		}
		return port;
	}
	
	/**
	 * 
	 * @param threadName is the thread name
	 * @param localUsername is the local username
	 * @param remoteUsername is the remote username
	 */
	public void createThread(String threadName, String localUsername, String remoteUsername) {
		ClientThread thread = null;
		try {
			ServerSocket servSocket = new ServerSocket(ACCEPT_PORT);
			Socket localSocket = servSocket.accept();
			Socket remoteSocket = new Socket("localhost", nextAvailablePort(remoteUsername));
			thread = new ClientThread(threadName, servSocket, localSocket, remoteSocket);
			threadsMap.get(localUsername).add(thread);
			threadsMap.get(remoteUsername).add(thread);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param threadName is the thread name
	 * @param localUsername is the local username
	 * @param remoteUsername is the remoet username
	 * @throws IOException
	 */
	public void destroyThread(String threadName, String localUsername, String remoteUsername) throws IOException {
		for (ClientThread thread : this.threadsMap.get(localUsername)) {
			if (thread.getName().equals(threadName)) {
				thread.getLocalSocket().close();
				thread.getRemoteSocket().close();
				threadsMap.get(localUsername).remove(thread);
				threadsMap.get(remoteUsername).remove(thread);
				break;
			}
		}
	}
}
