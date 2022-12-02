package main.java.com.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends UserThread {
	
	private ServerSocket serverSocket;
	private int serverPort;	
	
	public ServerThread(String threadID, int serverPort, String clientID) throws IOException {
		super(threadID, clientID);
		this.serverPort = serverPort;
		this.serverSocket = new ServerSocket(ACCEPT_PORT);
	}
	
	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}
	
	@Override
	public void run() {
		try {
	        System.out.println("About to accept client connection...");
	        Socket clientSocket = this.serverSocket.accept();
	        System.out.println("Accepted connection from " + clientSocket);
	        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
	        
	        ServerSocket server;
	        while(true) {
		        server = new ServerSocket(this.serverPort);
		        outputStream.writeInt(this.serverPort);
		        clientSocket.close();
		        clientSocket = server.accept();
		        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
		        String message = inputStream.readUTF();
		        System.out.println(message);
		        if (message.equals("end this.")) {
		        	break;
		        }
	        }
	        server.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
}
