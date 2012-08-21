package com.uppidy.android.sdk.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.uppidy.android.sdk.api.ApiMessage;
import com.uppidy.android.sdk.api.FeedOperations;
import com.uppidy.android.sdk.api.UppidyApi;

class FeedTemplate extends AbstractUppidyOperations implements FeedOperations {

	private final UppidyApi uppidyApi;

	public FeedTemplate(UppidyApi uppidyApi, boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.uppidyApi = uppidyApi;
	}

	@Override
	public String updateStatus(String message) {
		return post("me", message);
	}

	public String post(String ownerId, String message) {
		requireAuthorization();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.set("message", message);
		Map<String, Object> result = uppidyApi.publish(ownerId, "feed", map);
		return (String) result.get("id");
	}

	@Override
	public List<ApiMessage> searchFeed(String contextType, String contextId, String query, int offset, int limit, Date since, Date until) {
		requireAuthorization();
		return fetchConnectionList(contextType + "/" + contextId, "messages", query, offset, limit, since, until);
	}

	// private helpers
	private List<ApiMessage> fetchConnectionList(String objectId, String connectionName, String query, int offset, int limit, Date since, Date until) {
		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<String, String>();
		if (query != null && query.trim().length() > 0)
			queryParameters.set("query", query);
		if (offset > 0)
			queryParameters.set("offset", String.valueOf(offset));
		if (limit > 0)
			queryParameters.set("limit", String.valueOf(limit));
		if (since != null)
			queryParameters.set("since", String.valueOf(since.getTime()));
		if (until != null)
			queryParameters.set("until", String.valueOf(until.getTime()));
		return uppidyApi.fetchConnections(objectId, connectionName, ApiMessage.class, queryParameters);
	}

}
