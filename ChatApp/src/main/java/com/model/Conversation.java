package main.java.com.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Conversation {
	
	private Socket conversationSocket;
	
	public Conversation(Socket conversationSocket) {
		this.conversationSocket = conversationSocket;
	}
	
	/**
	 * 
	 * @return the read Message from the socket's ObjectInputStream
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Message read() throws ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(this.conversationSocket.getInputStream());
		Message message = (Message) in.readObject();
		in.close();
		return message;
	}
	
	/**
	 * 
	 * @param localUser is the local user
	 * @param remoteUser is the remote user
	 * @param messageContent is the message content
	 * @param date is the message date
	 * @throws IOException
	 * @throws SQLException
	 */
	public void write(User localUser, User remoteUser, String messageContent, LocalDateTime date) throws IOException, SQLException {
		Message message = new Message(localUser, remoteUser, messageContent, date, MessageType.MESSAGE);
		ObjectOutputStream out = new ObjectOutputStream(this.conversationSocket.getOutputStream());
		out.writeObject(message);
		out.close();
	}
	
	public void close() {
		try {
			this.conversationSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
