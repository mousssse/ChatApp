package main.java.com.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread {

	private ServerSocket servSocket;
	private Socket localSocket, remoteSocket;

	public ClientThread(String threadID, ServerSocket servSocket, Socket localSocket, Socket remoteSocket) {
		super(threadID);
		this.servSocket = servSocket;
		this.localSocket = localSocket;
		this.remoteSocket = remoteSocket;
	}

	public Socket getLocalSocket() {
		return this.localSocket;
	}
	
	public Socket getRemoteSocket() {
		return this.remoteSocket;
	}
	
	private void handleLocalSocket() throws IOException, InterruptedException {
		InputStream inputStream = this.localSocket.getInputStream();
		OutputStream outputStream = this.localSocket.getOutputStream();
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
		String nextLine;
		
		while ((nextLine = bufferReader.readLine()) != null) {
			if ("leavemealone".equalsIgnoreCase(nextLine)) {
				break;
			}
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
