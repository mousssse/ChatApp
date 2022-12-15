package main.java.com.model;
import java.io.Serializable;
import java.time.LocalDateTime;

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
		return "[" + this.date.toString() + "]" + " " +  this.fromUser.getUsername() + ": " + this.content;
	}

	public String getContent() {
		return this.content;
	}
	
	public LocalDateTime getDate() {
		return this.date;
	}
	
	public User getFromUser() {
		return this.fromUser;
	}

	public User getToUser() {
		return this.toUser;
	}

	public MessageType getType() {
		return this.type;
	}
}