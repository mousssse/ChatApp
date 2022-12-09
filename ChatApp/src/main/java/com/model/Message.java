package main.java.com.model;
import java.sql.Date;

public class Message {
	private String fromUserId;
	private String toUserId;
    private String content;
    private Date date;
    
	public Message(String fromUserId, String toUserId, String content) {
		super();
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.content = content;
		long millis = System.currentTimeMillis();
		this.date = new java.sql.Date(millis);
	}
	
}