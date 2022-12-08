package main.java.com.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class UserThread extends Thread {
	
	protected Socket socket;
	protected BufferedReader in;
	protected BufferedWriter out;

	/**
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public UserThread(Socket socket) throws IOException {
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public Socket getSocket() {
		return socket;
	}
	
	public void kill() throws IOException {
		this.in.close();
		this.out.close();
		this.socket.close();
	}
	
	

}
