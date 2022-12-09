package main.java.com.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.com.controller.AccountManager;
import main.java.com.controller.ThreadManager;

public class TCPServer implements Runnable {
	private static final int TCPserverPort = 1026;
	private int availablePort = 1027;
	private ServerSocket TCPserver;
	
	public static int getTCPserverPort() {
		return TCPserverPort;
	}
	
	@Override
	public void run() {
		try {
			this.TCPserver = new ServerSocket(TCPserverPort);
		} catch (IOException e) {
			e.printStackTrace();
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
				User otherUser = AccountManager.getInstance().getUserFromIP(socket.getInetAddress());
				User thisUser = AccountManager.getInstance().getUserFromIP(newServer.getInetAddress());
				ThreadManager.getInstance().addConversation(otherUser, new ConversationThread(socket, thisUser));
				System.out.println("TCP: Socket connected on new server");
				newServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
