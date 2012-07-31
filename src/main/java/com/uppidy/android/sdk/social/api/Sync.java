package com.uppidy.android.sdk.social.api;

import java.util.List;

/**
 * Transport object for the synchronization operation
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class Sync extends Extensible {

	private String clientVersion;
	private List<Message> messages;
	private List<Conversation> conversations;
	private List<Contact> contacts;
	private Location location;

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
