package com.uppidy.android.sdk.api.impl;

import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.uppidy.android.sdk.api.FeedOperations;
import com.uppidy.android.sdk.api.ApiMessage;
import com.uppidy.android.sdk.api.UppidyApi;

class FeedTemplate extends AbstractUppidyOperations implements FeedOperations {

	private final UppidyApi uppidyApi;
	
	public FeedTemplate(UppidyApi uppidyApi, boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.uppidyApi = uppidyApi;
	}

	public List<ApiMessage> getFeed() {
		return getFeed("me", 0, 25);
	}

	public List<ApiMessage> getFeed(int offset, int limit) {
		return getFeed("me", offset, limit);
	}

	public List<ApiMessage> getFeed(String ownerId) {
		return getFeed(ownerId, 0, 25);
	}
		
	public List<ApiMessage> getFeed(String ownerId, int offset, int limit) {
		requireAuthorization();
		return fetchConnectionList("me", "feed", null, offset, limit);
	}

	public List<ApiMessage> getHomeFeed() {
		return getHomeFeed(0, 25);
	}
	
	public java.util.List<ApiMessage> getHomeFeed(int offset, int limit) {
		requireAuthorization();
		return fetchConnectionList("me", "home", null, offset, limit);
	}

	public List<ApiMessage> getPosts() {
		return getPosts("me", 0, 25);
	}

	public List<ApiMessage> getPosts(int offset, int limit) {
		return getPosts("me", offset, limit);
	}

	public List<ApiMessage> getPosts(String ownerId) {
		return getPosts(ownerId, 0, 25);
	}
	
	public List<ApiMessage> getPosts(String ownerId, int offset, int limit) {
		requireAuthorization();
		return fetchConnectionList(ownerId, "posts", null, offset, limit);
	}
	
	public ApiMessage getPost(String entryId) {
		requireAuthorization();
		return uppidyApi.fetchObject(entryId, ApiMessage.class);
	}

	public String updateStatus(String message) {
		return post("me", message);
	}

	public String post(String ownerId, String message) {
		requireAuthorization();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.set("message", message);
		return uppidyApi.publish(ownerId, "feed", map);
	}

	public void deletePost(String id) {
		requireAuthorization();
		uppidyApi.delete(id);
	}

	public List<ApiMessage> searchPublicFeed(String query) {
		return searchPublicFeed(query, 0, 25);
	}
	
	public List<ApiMessage> searchPublicFeed(String query, int offset, int limit) {
		return fetchConnectionList("all", "posts", query, offset, limit);
	}
	
	public List<ApiMessage> searchHomeFeed(String query) {
		return searchHomeFeed(query, 0, 25);
	}
	
	public List<ApiMessage> searchHomeFeed(String query, int offset, int limit) {
		requireAuthorization();
		return fetchConnectionList("me", "home", query, offset, limit);
	}
	
	public List<ApiMessage> searchUserFeed(String query) {
		return searchUserFeed("me", query, 0, 25);
	}

	public List<ApiMessage> searchUserFeed(String query, int offset, int limit) {
		return searchUserFeed("me", query, offset, limit);
	}

	public List<ApiMessage> searchUserFeed(String userId, String query) {
		return searchUserFeed(userId, query, 0, 25);
	}
	
	public List<ApiMessage> searchUserFeed(String userId, String query, int offset, int limit) {
		requireAuthorization();
		return fetchConnectionList(userId, "feed", query, offset, limit);
	}
	
	// private helpers
	
	private List<ApiMessage> fetchConnectionList(String objectId, String connectionName, String query, int offset, int limit) {
		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<String, String>();
		if(query != null && query.trim().length() > 0) queryParameters.set("query", query);
		if(offset > 0)	queryParameters.set("offset", String.valueOf(offset));
		if(limit > 0) queryParameters.set("limit", String.valueOf(limit));
		return uppidyApi.fetchConnections(objectId, connectionName, ApiMessage.class, queryParameters);
	}

}
