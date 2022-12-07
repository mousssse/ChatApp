/**
 * The ClientThread class is an active conversation between two online users.
 */

package main.java.com.model;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class ClientThread extends UserThread {

	private String serverIP;
	private Socket clientSocket;

	/**
	 * TODO RADICAL CHANGES COMING
	 * @param serverID is the ID of the user playing the role of the server
	 * @param serverIP is the IP of the user playing the role of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ClientThread(String serverID, String serverIP) throws UnknownHostException, IOException {
		super(serverID);
		this.serverIP = serverIP;
		this.clientSocket = new Socket(this.serverIP, ACCEPT_PORT);
	}
	
	public Socket getClientSocket() {
		return this.clientSocket;
	}
	
	@Override
	public void run() {
		try {
			// The client receives the conversation-specific port from the server
	        DataInputStream newServerInput = new DataInputStream(this.clientSocket.getInputStream());
	        int serverPort = newServerInput.readInt();
	        this.clientSocket.close();
	        this.clientSocket = new Socket(this.serverIP, serverPort);
	        // Chat is now running
	        DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());
	        out.writeUTF("Coucou!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
