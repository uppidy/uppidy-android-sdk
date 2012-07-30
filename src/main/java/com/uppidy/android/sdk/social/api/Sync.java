package com.uppidy.android.sdk.social.api;

import java.util.List;

import com.uppidy.util.StringUtil;

/**
 * Transport object for the synchronization operation, it has a list of contacts, conversations and messages
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class Sync {

	private String clientVersion;
	private List<Message> messages;
	private List<Conversation> conversations;
	private List<Contact> contacts;
	private Double latitude;
	private Double longitude;

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public List<Conversation> getConversations() {
		return conversations;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public void setConversations(List<Conversation> conversations) {
		this.conversations = conversations;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * This method create a string in json format using the content of nested  vectors.
	 * 
	 * @return string
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("{\"messages\":[");
		sb.append(StringUtil.csv(messages));
		sb.append("],\"conversations\":[");
		sb.append(StringUtil.csv(conversations));
		sb.append("],\"contacts\":[");
		sb.append(StringUtil.csv(contacts));
		sb.append("]}");
		return sb.toString();
	}

}
