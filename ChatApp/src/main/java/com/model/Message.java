package main.java.com.model;
import java.time.LocalDateTime;

/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class Message {
	private User fromUser;
	private User toUser;
    private String content;
    private LocalDateTime date;
    private MessageType type;
    
	public Message(User fromUser, User toUser, String content, MessageType type) {
		super();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.date = LocalDateTime.now();
		this.type = type;
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