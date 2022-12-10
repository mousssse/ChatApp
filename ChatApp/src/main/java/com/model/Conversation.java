package main.java.com.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import main.java.com.controller.DBManager;

/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class Conversation {
	
	private Socket conversationSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	/**
	 * 
	 * @param conversationSocket is the socket associated to the conversation
	 */
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
	
	/**
	 * 
	 * @param localUser is the local user
	 * @param remoteUser is the remote user
	 * @param content is the message content
	 * @throws IOException
	 * @throws SerialException
	 * @throws SQLException
	 */
	public void write(User localUser, User remoteUser, String content) throws IOException, SerialException, SQLException {
		Message message = new Message(localUser, remoteUser, content, MessageType.MESSAGE);
		out.writeObject(message);
		DBManager.getInstance().insertMessage(new SerialBlob(message.getContent().getBytes()), message.getDate().toString(), message.getFromUser().getId(), message.getToUser().getId());
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
