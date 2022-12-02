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

import main.java.com.controller.ThreadManager;

/**
 * 
 * @author Sandro
 * @author sarah
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

	public ServerSocket getServSocket() {
		return servSocket;
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
	private void handleSockets() throws IOException, InterruptedException {
		InputStream localInputStream = this.localSocket.getInputStream();
		InputStream remoteInputStream = this.remoteSocket.getInputStream();
		OutputStream localOutputStream = this.localSocket.getOutputStream();
		OutputStream remoteOutputStream = this.remoteSocket.getOutputStream();
		BufferedReader localBufferReader = new BufferedReader(new InputStreamReader(localInputStream));
		BufferedReader remoteBufferReader = new BufferedReader(new InputStreamReader(remoteInputStream));
		
		String localNextLine, remoteNextLine;
		
		while ((localNextLine = localBufferReader.readLine()) != null && (remoteNextLine = remoteBufferReader.readLine()) != null) {
			String localOutput = "You typed: " + localNextLine + "\n";
			String remoteOutput = "You typed: " + remoteNextLine + "\n";
			localOutputStream.write(localOutput.getBytes());
			remoteOutputStream.write(remoteOutput.getBytes());
		}
		
		//ThreadManager.threadManager.destroyThread(this.getName(), localSocket, remoteNextLine);
	}

	@Override
	public void run() {
		try {
			handleSockets();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
