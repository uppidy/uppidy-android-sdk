package com.uppidy.android.sdk.social.api;

import java.util.Date;
import java.util.List;

/**
 * Model class representing an entry in a feed or chat conversation. 
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class Message {
	
	private String id;
	
	private Reference from;

	private List<Reference> to;

	private String text;
	
	private boolean sent;

	private Date sentTime;

	private Date createdTime;

	private Date updatedTime;
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public Reference getFrom() {
		return from;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public boolean isSent() {
		return sent;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public List<Reference> getTo() {
		return to;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setFrom(Reference from) {
		this.from = from;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public void setTo(List<Reference> to) {
		this.to = to;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
}
