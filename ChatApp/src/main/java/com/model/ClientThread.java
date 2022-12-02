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
	 * 
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
	
//	/**
//	 * 
//	 * @throws IOException
//	 * @throws InterruptedException
//	 */
//	private void handleSocket() throws IOException, InterruptedException {
//		DataInputStream localInputStream = new DataInputStream(this.localSocket.getInputStream());
//		DataInputStream remoteInputStream = new DataInputStream(this.remoteSocket.getInputStream());
//		DataOutputStream localOutputStream = new DataOutputStream(this.localSocket.getOutputStream());
//		DataOutputStream remoteOutputStream =  new DataOutputStream(this.remoteSocket.getOutputStream());
//		BufferedReader localBufferReader = new BufferedReader(new InputStreamReader(localInputStream));
//		BufferedReader remoteBufferReader = new BufferedReader(new InputStreamReader(remoteInputStream));
//		
//		localOutputStream.writeInt(1);
//		String localNextLine, remoteNextLine;
//		boolean end = false;
//		
//		while (!end) {
//			if ((localNextLine = localBufferReader.readLine()) != null) {
//				String localOutput = "You typed: " + localNextLine + "\n";
//				localOutputStream.write(localOutput.getBytes());
//			}
//			if ((remoteNextLine = remoteBufferReader.readLine()) != null) {
//				String remoteOutput = "They typed: " + remoteNextLine + "\n";
//				remoteOutputStream.write(remoteOutput.getBytes());
//			}
//		}
//		
//		//ThreadManager.threadManager.destroyThread(this.getName(), localSocket, remoteNextLine);
//	}

}
