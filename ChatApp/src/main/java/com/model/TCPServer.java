package main.java.com.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;

/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class TCPServer implements Runnable {
	private static int TCPserverPort = 1026;
	private int availablePort;
	private ServerSocket TCPserver = null;
	
	/**
	 * 
	 * @return the TCP server port.
	 */
	public static int getTCPserverPort() {
		return TCPserverPort;
	}
	
	@Override
	public void run() {
		while (this.TCPserver == null) {
			try {
				this.TCPserver = new ServerSocket(TCPserverPort);
				this.availablePort = TCPserverPort + 1;
			} catch (IOException e) {
				TCPserverPort++;
			}
		}
		while (true) {
			try {
				// Step 1: listening on TCPserverPort
				System.out.println("TCP: Waiting for someone to connect");
				Socket socket = this.TCPserver.accept();
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				// Step 2: sending the redirection port
				ServerSocket newServer = new ServerSocket(this.availablePort);
				out.writeInt(this.availablePort++);
				socket.close();
				
				// Step 3: socket is now on the new port
				socket = newServer.accept();
				System.out.println("TCP: Socket connected on new server");

				User remoteUser = OnlineUsersManager.getInstance().getUserFromIP(socket.getInetAddress());
				Conversation conversation = new Conversation(socket);
				
				// TODO onChatRequested listener missing
				ThreadManager.getInstance().addConversation(remoteUser, conversation);
				new Thread(new ConversationThread(conversation, remoteUser), "Conversation with " + remoteUser.getUsername()).start();
				
				newServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
