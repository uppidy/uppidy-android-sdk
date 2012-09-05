package com.uppidy.android.sdk.api;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.util.MultiValueMap;

/**
 * Transport object for the synchronization operation
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiSync extends ApiEntity {

	private String clientVersion;

	private ApiLocation location;
	
	private List<ApiMessage> messages;
	
	private List<ApiConversation> conversations;
	
	private List<ApiContact> contacts;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	@JsonSerialize(include = Inclusion.NON_NULL)
	public ApiLocation getLocation() {
		return location;
	}

	public void setLocation(ApiLocation location) {
		this.location = location;
	}
	
	@JsonSerialize(include = Inclusion.NON_NULL)
	public List<ApiMessage> getMessages() {
		return messages;
	}

	@JsonSerialize(include = Inclusion.NON_NULL)
	public List<ApiContact> getContacts() {
		return contacts;
	}

	@JsonSerialize(include = Inclusion.NON_NULL)
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
	
	@Override
	public void copyFromRef(MultiValueMap<String, ApiEntity> map) {
		super.copyFromRef(map);
		copyFromRefs(messages, map);
		copyFromRefs(contacts, map);
		copyFromRefs(conversations, map);
	}
}
