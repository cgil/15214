package edu.cmu.cs.cs214.hw9.backend;

import java.util.Date;

public class Status {

	private final String message;
	private final User poster;
	private final Date timestamp;
	
	public Status(String m, User p, Date time){
		message = m;
		poster = p;
		timestamp = time;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the poster
	 */
	public User getPoster() {
		return poster;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
}
