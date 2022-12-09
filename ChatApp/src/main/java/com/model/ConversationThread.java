package main.java.com.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConversationThread implements Runnable {
	
	private Socket conversationSocket;
	private User remoteUser;
	private DataInputStream in;
	private DataOutputStream out;
	
	public ConversationThread(Socket conversationSocket, User remoteUser) {
		this.conversationSocket = conversationSocket;
		this.remoteUser = remoteUser;
		try {
			this.in = new DataInputStream(this.conversationSocket.getInputStream());
			this.out = new DataOutputStream(this.conversationSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public User getRemoteUser() {
		return this.remoteUser;
	}
	
	public void close() {
		try {
			this.conversationSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			// TCP: listening to receiving messages/convo being ended
			// UDP: listening to the other disconnecting
		}
	}
}
