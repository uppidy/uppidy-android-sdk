package com.uppidy.android.sdk.api;

import java.util.List;

/**
 * Conversation, i.e. list of SMS / MMS messages with specific contact or iMessage group chat
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiConversation extends ApiExtensible {
	private String id;
	private String name;
	private List<ApiContactInfo> members;
	
	public List<ApiContactInfo> getMembers() {
		return members;
	}

	public void setMembers(List<ApiContactInfo> members) {
		this.members = members;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	// TODO (AR): check if hashCode and equals should be expanded
		
	@Override
	public int hashCode() {
		int result = 1;
		result = 73 * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof ApiConversation)) {
			return false;
		}
		
		ApiConversation other = (ApiConversation) obj;
		if (id == null || other.id == null) {
			return false;						
		}
		return id.equals(other.id);
	}
		
}
