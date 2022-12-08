/**
 * The ThreadManager manages all active conversations for all online users.
 */

package main.java.com.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.com.controller.listener.ChatListener;
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
public class ThreadManager implements ChatListener {
	// Original TCP acceptance port
	public final static int TCP_ACCEPT_PORT = 1026;
	// Original TCP acceptance port
	public final static int UDP_ACCEPT_PORT = 1025;
	// The ThreadManager is a singleton
	private static final ThreadManager threadManager = new ThreadManager();
	// Mapping of online users and their running conversations
	private Map<User, ArrayList<UserThread>> threadsMap;
	// Next available port of the server socket (works by increments of 1)
	private int nextAvailablePort = 1027;
	
	private ThreadManager() {
		this.threadsMap = new HashMap<User, ArrayList<UserThread>>();
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
		this.threadsMap.put(user, new ArrayList<UserThread>());
	}
	
	/**
	 * Removes a user from the ThreadManager's map.
	 * @param user is the user object
	 */
	public void removeUser(User user) {
		this.threadsMap.remove(user);
	}

	/**
	 * TODO This is not okay yet
	 */
	public void onChatRequestReceived(User user, int port) {
		try {
			DatagramSocket UDPServer = new DatagramSocket(UDP_ACCEPT_PORT);
			ServerSocket TCPServer = new ServerSocket(TCP_ACCEPT_PORT);

			byte[] content = new byte[5];
			DatagramPacket received = new DatagramPacket(content, 5);
			UDPServer.receive(received);

			DatagramPacket packet = new DatagramPacket("1025".getBytes(), 4, user.getUserIP(), received.getPort());
			UDPServer.send(packet);
			
			while(true) {
				Socket TCPSocket = TCPServer.accept(); 
				
				// Redirecting the communication to a new port
				DataOutputStream out = new DataOutputStream(TCPSocket.getOutputStream());
				out.writeInt(nextAvailablePort++);

				// Creating the ServerThread
				ServerSocket serverSocket = new ServerSocket(port);
				Socket socket = serverSocket.accept();
				UserThread thread = new ServerThread(serverSocket, socket);
				threadsMap.get(user).add(thread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChatRequestSent(User user, int port) {
		// TODO Auto-generated method stub
		try {
			Socket socket = new Socket(user.getUserIP(), port);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			// Waits for the new port from the server, closes the socket and creates a new one on the new port
			int redirection = in.readInt();
			socket.close();
			socket = new Socket(user.getUserIP(), redirection);
			UserThread thread = new ClientThread(socket);
			threadsMap.get(user).add(thread);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onChatClosureRequested(User user, UserThread thread) {
		// TODO Auto-generated method stub
		threadsMap.get(user).remove(thread);
	}

	@Override
	public void onMessageToSend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageToReceive() {
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
