package main.java.com.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import main.java.com.controller.DBManager;

public class Conversation {
	
	private Socket conversationSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public Conversation(Socket conversationSocket) {
		this.conversationSocket = conversationSocket;
		try {
			this.in = new ObjectInputStream(this.conversationSocket.getInputStream());
			this.out = new ObjectOutputStream(this.conversationSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Message read() throws ClassNotFoundException, IOException {
		return (Message) in.readObject();
	}
	
	public void write(String localUserId, String remoteUserId, String messageContent) throws IOException, SerialException, SQLException {
		Message message = new Message(localUserId, remoteUserId, messageContent, MessageType.MESSAGE);
		out.writeObject(message);
		DBManager.getInstance().insertMessage(new SerialBlob(message.getContent().getBytes()), message.getDate().toString(), message.getFromUserId(), message.getToUserId());
	}
	
	public void close() {
		try {
			this.in.close();
			this.out.close();
			this.conversationSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
