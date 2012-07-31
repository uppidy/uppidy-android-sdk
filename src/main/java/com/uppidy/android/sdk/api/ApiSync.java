package com.uppidy.android.sdk.api;

import java.util.List;

/**
 * Transport object for the synchronization operation
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiSync extends ApiExtensible {

	private String clientVersion;
	private List<ApiMessage> messages;
	private List<ApiConversation> conversations;
	private List<ApiContact> contacts;
	private ApiLocation location;

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public List<ApiMessage> getMessages() {
		return messages;
	}

	public List<ApiContact> getContacts() {
		return contacts;
	}

	public List<ApiConversation> getConversations() {
		return conversations;
	}

	public void setMessages(List<ApiMessage> messages) {
		this.messages = messages;
	}

	public void setContacts(List<ApiContact> contacts) {
		this.contacts = contacts;
	}

	public void setConversations(List<ApiConversation> conversations) {
		this.conversations = conversations;
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
