package com.uppidy.android.sdk.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.uppidy.android.sdk.api.ApiBodyPart;
import com.uppidy.android.sdk.api.ApiBodyPartResource;
import com.uppidy.android.sdk.api.ApiContact;
import com.uppidy.android.sdk.api.ApiContainer;
import com.uppidy.android.sdk.api.ApiConversation;
import com.uppidy.android.sdk.api.ApiEntity;
import com.uppidy.android.sdk.api.ApiMessage;
import com.uppidy.android.sdk.api.ApiModifications;
import com.uppidy.android.sdk.api.ApiSync;
import com.uppidy.android.sdk.api.BackupOperations;
import com.uppidy.android.sdk.api.UppidyApi;

// TODO (AR): add missing implementations
class BackupTemplate extends AbstractUppidyOperations implements BackupOperations {

	private final UppidyApi uppidyApi;

	public BackupTemplate(UppidyApi uppidyApi, boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.uppidyApi = uppidyApi;
	}

	@Override
	public List<ApiContainer> listContainers(Map<String, String> queryParams) {
		requireAuthorization();
		return uppidyApi.fetchConnections("me", "containers", ApiContainer.class, toMultiValueMap(queryParams));
	}

	@Override
	public ApiModifications saveContainer(ApiContainer data) {
		requireAuthorization();
		ApiModifications result = uppidyApi.publish("me", "containers", ApiModifications.class, data);
		data.copyFromRef(result.refsToEntities());
		return result;
	}

	@Override
	public void deleteContainer(String containerId) {
		requireAuthorization();
		uppidyApi.delete("me/containers/" + containerId);
	}

	@Override
	public ApiModifications sync(String containerId, ApiSync data) {
		requireAuthorization();
		ApiModifications result = uppidyApi.publish("me/containers/" + containerId, "sync", ApiModifications.class, data);
		data.copyFromRef(result.refsToEntities());
		return result;
	}

	@Override
	public ApiModifications upload(String containerId, List<ApiBodyPart> parts) {
		requireAuthorization();
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
		for(ApiBodyPart part : parts) {
			params.add("id", part.getId());
			params.add("ref", part.getRef());
			params.add("data", new ApiBodyPartResource(part));
		}
		ApiModifications result = uppidyApi.publish("me/containers/" + containerId, "attachments", ApiModifications.class, params);
		ApiEntity.copyFromRefs(parts, result.refsToEntities());
		return result;
	}

	@Override
	public List<ApiContact> listContacts(String containerId, Map<String, String> queryParams) {
		requireAuthorization();
		return uppidyApi.fetchConnections("me/containers/" + containerId, "contacts", ApiContact.class, toMultiValueMap(queryParams));
	}

	@Override
	public List<ApiMessage> listMessages(String containerId, Map<String, String> queryParams) {
		requireAuthorization();
		return uppidyApi.fetchConnections("me/containers/" + containerId, "messages", ApiMessage.class, toMultiValueMap(queryParams));
	}

	@Override
	public List<ApiConversation> listConversations(String containerId, Map<String, String> queryParams) {
		requireAuthorization();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.setAll(queryParams);
		return uppidyApi.fetchConnections("me/containers/" + containerId, "conversations", ApiConversation.class, params);
	}

	private Date getSyncDate(String containerId, String type) {
		requireAuthorization();
		String objectId = "me/containers/" + containerId + "/sync/" + type;
		Date result = uppidyApi.fetchObject(objectId, Date.class);
		/*
		 * if(result == null) { result = Calendar.getInstance().getTime(); }
		 */
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
		if (queryParams != null)
			params.setAll(queryParams);
		return params;
	}
}
