package main.java.com.model;

import java.sql.Date;
import java.sql.Time;

public class Message {
	// Random comment because I need to test something
    private String content;
    private Date date;
    private Time time;
    
	public Message(String content, Date date, Time time) {
		super();
		this.content = content;
		this.date = date;
		this.time = time;
	}
}