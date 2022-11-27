/**
 * The ClientThread class is an active conversation between two online users.
 */


package main.java.com.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Sandro
 * @author Sarah
 *
 */
public class ClientThread extends Thread {

	private ServerSocket servSocket;
	private Socket localSocket, remoteSocket;

	/**
	 * 
	 * @param threadID Thread name.
	 * @param servSocket corresponds to the local user's ServerSocket instance.
	 * @param localSocket corresponds to the local user's Socket instance.
	 * @param remoteSocket corresponds to the remote user's Socket instance.
	 */
	public ClientThread(String threadID, ServerSocket servSocket, Socket localSocket, Socket remoteSocket) {
		super(threadID);
		this.servSocket = servSocket;
		this.localSocket = localSocket;
		this.remoteSocket = remoteSocket;
	}

	/**
	 * 
	 * @return Socket of the local user
	 */
	public Socket getLocalSocket() {
		return this.localSocket;
	}
	
	/**
	 * 
	 * @return Socket of the remote user
	 */
	public Socket getRemoteSocket() {
		return this.remoteSocket;
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void handleLocalSocket() throws IOException, InterruptedException {
		InputStream inputStream = this.localSocket.getInputStream();
		OutputStream outputStream = this.localSocket.getOutputStream();
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
		String nextLine;
		
		while ((nextLine = bufferReader.readLine()) != null) {
			String output = "You typed: " + nextLine + "\n";
			outputStream.write(output.getBytes());
		}
	}

	@Override
	public void run() {
		try {
			handleLocalSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
