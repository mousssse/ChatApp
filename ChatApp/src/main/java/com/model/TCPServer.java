package main.java.com.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {
	private static final int TCPserverPort = 1026;
	private int availablePort = 1027;
	private ServerSocket TCPserver;
	
	public TCPServer() {
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
				out.writeInt(this.availablePort);
				socket.close();
				
				// socket is now on the new server
				socket = newServer.accept();
				System.out.println("TCP: Socket connected on new server");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
