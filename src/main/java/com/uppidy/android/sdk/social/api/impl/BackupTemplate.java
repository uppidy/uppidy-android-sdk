package com.uppidy.android.sdk.social.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.uppidy.android.sdk.social.api.BackupOperations;
import com.uppidy.android.sdk.social.api.Contact;
import com.uppidy.android.sdk.social.api.Container;
import com.uppidy.android.sdk.social.api.Conversation;
import com.uppidy.android.sdk.social.api.Message;
import com.uppidy.android.sdk.social.api.Sync;
import com.uppidy.android.sdk.social.api.UppidyApi;

// TODO (AR): add missing implementations
class BackupTemplate extends AbstractUppidyOperations implements BackupOperations {

	private final UppidyApi uppidyApi;

	public BackupTemplate(UppidyApi uppidyApi, boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.uppidyApi = uppidyApi;
	}

	@Override
	public List<Container> listContainers(Map<String, String> queryParams) {
		requireAuthorization();
		return uppidyApi.fetchConnections("me", "containers", Container.class, toMultiValueMap(queryParams));
	}

	@Override
	public Container createContainer(Map<String, Object> parameters) {
		requireAuthorization();
		return uppidyApi.publish("me", "containers", Container.class, parameters);
	}

	@Override
	public Container updateContainer(String containerId, Map<String, Object> parameters) {
		requireAuthorization();
		return uppidyApi.publish("me/containers/" + containerId, "data", Container.class, parameters);
	}

	@Override
	public void deleteContainer(String containerId) {
		requireAuthorization();
		uppidyApi.delete("me/containers/" + containerId);
	}

	@Override
	public void sync(String containerId, Sync data) {
		requireAuthorization();
		uppidyApi.post("me/containers/" + containerId, "sync", data);
	}
	
	@Override
	public List<Contact> listContacts(String containerId, Map<String, String> queryParams) {
		requireAuthorization();
		return uppidyApi.fetchConnections("me/containers/" + containerId, "contacts", Contact.class, toMultiValueMap(queryParams));
	}

	@Override
	public List<Message> listMessages(String containerId, Map<String, String> queryParams) {
		requireAuthorization();
		return uppidyApi.fetchConnections("me/containers/" + containerId, "messages", Message.class, toMultiValueMap(queryParams));
	}

	@Override
	public List<Conversation> listConversations(String containerId, Map<String, String> queryParams) {
		requireAuthorization();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.setAll(queryParams);
		return uppidyApi.fetchConnections("me/containers/" + containerId, "conversations", Conversation.class, params);
	}

	private Date getSyncDate(String containerId, String type) {
		requireAuthorization();
		String objectId = "me/containers/" + containerId + "/sync/" + type;
		Date result = uppidyApi.fetchObject(objectId, Date.class);
		if(result == null) {
			result = Calendar.getInstance().getTime();
		}
		return result;
	}

	@Override
	public Date getFirstMessageSyncDate(String containerId) {
		return getSyncDate(containerId, "first");
	}

	@Override
	public Date getLastMessageSyncDate(String containerId) {
		return getSyncDate(containerId, "last");
	}
	
	private <T> MultiValueMap<String, T> toMultiValueMap(Map<String, T> queryParams) {
		MultiValueMap<String, T> params = new LinkedMultiValueMap<String, T>();
		if(queryParams != null)	params.setAll(queryParams);
		return params;		
	}
}
