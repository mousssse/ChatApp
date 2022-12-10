package main.java.com.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
	
	public void write(User localUser, User remoteUser, String messageContent, LocalDateTime date) throws IOException, SQLException {
		Message message = new Message(localUser, remoteUser, messageContent, date, MessageType.MESSAGE);
		out.writeObject(message);
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
