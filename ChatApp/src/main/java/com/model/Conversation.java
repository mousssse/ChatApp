package main.java.com.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Conversation is the socket associated to a conversation between the local user and one other user.
 * @author sarah
 * @author Sandro
 *
 */
public class Conversation {
	
	private Socket conversationSocket;
	
	public Conversation(Socket conversationSocket) {
		this.conversationSocket = conversationSocket;
	}
	
	/**
	 * 
	 * @return the read Message from the socket's ObjectInputStream
	 * @throws ClassNotFoundException - ClassNotFoundException
	 * @throws IOException - IOException
	 */
	public Message read() throws ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(this.conversationSocket.getInputStream());
		Message message = (Message) in.readObject();
		return message;
	}
	
	/**
	 * 
	 * @param localUser is the local user
	 * @param remoteUser is the remote user
	 * @param messageContent is the message content
	 * @param date is the message date
	 * @throws IOException - IOException
	 * @throws SQLException - SQLException
	 */
	public void write(User localUser, User remoteUser, String messageContent, LocalDateTime date) throws IOException, SQLException {
		Message message = null;
		// The empty string isn't an allowed message. It's only used to detect the X button click of the ChatFrame.
		if (messageContent.equals("")) {
			message = new Message(localUser, remoteUser, messageContent, date, MessageType.CLOSING_CONVERSATION);
		} else {
			message = new Message(localUser, remoteUser, messageContent, date, MessageType.MESSAGE);
		}
		ObjectOutputStream out = new ObjectOutputStream(this.conversationSocket.getOutputStream());
		out.writeObject(message);
	}
	
	public void close() {
		try {
			this.conversationSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
