package com.uppidy.android.sdk.api;

import java.util.Date;
import java.util.List;

/**
 * Model class representing an entry in a feed or chat conversation. 
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiMessage {
	
	private String id;
	
	private ApiContactInfo from;

	private List<ApiContactInfo> to;

	private String text;
	
	private boolean sent;

	private Date sentTime;

	private Date createdTime;

	private Date updatedTime;
	
	private ApiLocation location;
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public ApiContactInfo getFrom() {
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

	public List<ApiContactInfo> getTo() {
		return to;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setFrom(ApiContactInfo from) {
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

	public void setTo(List<ApiContactInfo> to) {
		this.to = to;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * @return the location
	 */
	public ApiLocation getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(ApiLocation location) {
		this.location = location;
	}
	
}