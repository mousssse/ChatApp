package main.java.com.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends UserThread {
	
	private ServerSocket serverSocket;
	private int serverPort;	
	
	/**
	 * TODO RADICAL CHANGES COMING
	 * @param serverPort is the port of the server socket
	 * @param clientID is the ID of the user playing the role of the client
	 * @throws IOException
	 */
	public ServerThread(int serverPort, String clientID) throws IOException {
		super(clientID);
		this.serverPort = serverPort;
		this.serverSocket = new ServerSocket(ACCEPT_PORT);
	}
	
	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}
	
	@Override
	public void run() {
		try {
			/** TODO the first while(true) is because a user should always be listening for a connection on 9000. Is this correct?
			 * How come a user is always listening, even though an instance of this class is only created when a conversation is
			 * requested?
			 * 
			 */
			while(true) {
				// Listens for a connection to be made on port ACCEPT_PORT = 9000 and accepts it
		        Socket clientSocket = this.serverSocket.accept();
		        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
		        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
		        
		        // Redirected the client for the rest of the communication to the socket whose port is serverPort
		        ServerSocket server;
		        while(true) {
			        server = new ServerSocket(this.serverPort);
			        // Sends the client the new socket port on which to communicate
			        outputStream.writeInt(this.serverPort);
			        // Close the previous socket
			        clientSocket.close();
			        // Listens for a connection to be made on port serverPort and accepts it
			        clientSocket = server.accept();
			        // Chat is now running
			        DataInputStream chatInputStream = new DataInputStream(clientSocket.getInputStream());
			        String message = chatInputStream.readUTF();
			        System.out.println(message);
			        // 'End chat' requested by client
			        if (message.equals("END-CHAT")) {
			        	break;
			        }
		        }
		        // Server closed on chat end
		        server.close();
			}
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
}
