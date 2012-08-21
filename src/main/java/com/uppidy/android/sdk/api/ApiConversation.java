package com.uppidy.android.sdk.api;

import java.util.List;

/**
 * Conversation, i.e. list of SMS / MMS messages with specific contact or
 * iMessage group chat
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiConversation extends ApiEntity {

	private String name;

	private List<ApiContactInfo> members;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ApiContactInfo> getMembers() {
		return members;
	}

	public void setMembers(List<ApiContactInfo> members) {
		this.members = members;
	}

}
