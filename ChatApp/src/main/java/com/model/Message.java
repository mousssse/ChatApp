package main.java.com.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = -2950854009692914838L;
	private User fromUser;
	private User toUser;
    private String content;
    private LocalDateTime date;
    private MessageType type;
    
	public Message(User fromUser, User toUser, String content, LocalDateTime date, MessageType type) {
		super();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.date = date;
		this.type = type;
	}
	
	/**
	 * Message as displayed in the chat frame
	 */
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return "[" + this.date.format(formatter) + "]" + " " +  this.fromUser.getUsername() + ": " + this.content;
	}

	/**
	 * 
	 * @return the message content
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * 
	 * @return the message's date and time.
	 */
	public LocalDateTime getDate() {
		return this.date;
	}
	
	/**
	 * 
	 * @return the sending user.
	 */
	public User getFromUser() {
		return this.fromUser;
	}

	/**
	 * 
	 * @return the receiving user.
	 */
	public User getToUser() {
		return this.toUser;
	}

	/**
	 * 
	 * @return the type of the message.
	 */
	public MessageType getType() {
		return this.type;
	}
}