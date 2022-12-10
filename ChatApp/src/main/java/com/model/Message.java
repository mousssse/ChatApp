package main.java.com.model;
import java.time.LocalDateTime;

public class Message {
	private String fromUserId;
	private String toUserId;
    private String content;
    private LocalDateTime date;
    private MessageType type;
    
	public Message(String fromUserId, String toUserId, String content, MessageType type) {
		super();
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
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
	
	public String getFromUserId() {
		return this.fromUserId;
	}
	
	
	public String getToUserId() {
		return this.toUserId;
	}
	
	public MessageType getType() {
		return this.type;
	}
}