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

	//private Socket localSocket, remoteSocket;
	private String serverIP;
	private Socket clientSocket;

	/**
	 * 
	 * @param threadID Thread name.
	 * @param servSocket corresponds to the local user's ServerSocket instance.
	 * @param localSocket corresponds to the local user's Socket instance.
	 * @param remoteSocket corresponds to the remote user's Socket instance.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
//	public ClientThread(String threadID, Socket localSocket, Socket remoteSocket) {
//		super(threadID);
//		this.localSocket = localSocket;
//		this.remoteSocket = remoteSocket;
//	}
	
	public ClientThread(String threadID, String serverID, String serverIP) throws UnknownHostException, IOException {
		super(threadID, serverID);
		this.serverIP = serverIP;
		this.clientSocket = new Socket(this.serverIP, ACCEPT_PORT);
	}
	
	public Socket getClientSocket() {
		return this.clientSocket;
	}

	/**
	 * 
	 * @return Socket of the local user
	 */
//	public Socket getLocalSocket() {
//		return this.localSocket;
//	}
//	
//	/**
//	 * 
//	 * @return Socket of the remote user
//	 */
//	public Socket getRemoteSocket() {
//		return this.remoteSocket;
//	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
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

	@Override
	public void run() {
		try {
	        DataInputStream in = new DataInputStream(this.clientSocket.getInputStream());
	        int serverPort = in.readInt();
	        this.clientSocket.close();
	        
	        // sending one message
	        this.clientSocket = new Socket(this.serverIP, serverPort);
	        DataOutputStream out = new DataOutputStream(this.clientSocket.getOutputStream());
	        out.writeUTF("Coucou!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
