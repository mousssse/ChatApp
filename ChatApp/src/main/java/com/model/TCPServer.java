package main.java.com.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;

public class TCPServer implements Runnable {
	private static int TCPserverPort = 1026;
	private int availablePort;
	private ServerSocket TCPserver = null;
	
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
				System.out.println("TCP: Waiting for someone to connect");
				Socket socket = this.TCPserver.accept();
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				// giving redirection port
				ServerSocket newServer = new ServerSocket(this.availablePort);
				out.writeInt(this.availablePort++);
				socket.close();
				
				// socket is now on the new server
				socket = newServer.accept();
				System.out.println("TCP: Socket connected on new server");

				// launch listening TCP server for this connection
				User remoteUser = OnlineUsersManager.getInstance().getUserFromIP(socket.getInetAddress());
				Conversation conversation = new Conversation(socket);
				// TODO onChatRequested listener missing
				ThreadManager.getInstance().addConversation(remoteUser, conversation);
				new Thread(new ConversationServer(conversation, remoteUser), "Conversation with " + remoteUser.getUsername()).start();;
				
				newServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
