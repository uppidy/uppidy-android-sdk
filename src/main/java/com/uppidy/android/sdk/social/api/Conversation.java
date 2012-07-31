package com.uppidy.android.sdk.social.api;

import java.util.List;

/**
 * Conversation, i.e. list of SMS / MMS messages with specific contact or iMessage group chat
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class Conversation extends Extensible {
	private String id;
	private String name;
	private List<ContactInfo> members;
	
	public List<ContactInfo> getMembers() {
		return members;
	}

	public void setMembers(List<ContactInfo> members) {
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
		if (obj == null || !(obj instanceof Conversation)) {
			return false;
		}
		
		Conversation other = (Conversation) obj;
		if (id == null || other.id == null) {
			return false;						
		}
		return id.equals(other.id);
	}
		
}
