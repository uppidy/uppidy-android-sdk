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
	
	private ContactInfo from;

	private List<ContactInfo> to;

	private String text;
	
	private boolean sent;

	private Date sentTime;

	private Date createdTime;

	private Date updatedTime;
	
	private Location location;
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public ContactInfo getFrom() {
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

	public List<ContactInfo> getTo() {
		return to;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setFrom(ContactInfo from) {
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

	public void setTo(List<ContactInfo> to) {
		this.to = to;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
