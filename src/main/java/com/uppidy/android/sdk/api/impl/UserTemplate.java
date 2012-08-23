package com.uppidy.android.sdk.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.uppidy.android.sdk.api.ApiContactInfo;
import com.uppidy.android.sdk.api.ApiProfile;
import com.uppidy.android.sdk.api.UppidyApi;
import com.uppidy.android.sdk.api.UserOperations;

class UserTemplate extends AbstractUppidyOperations implements UserOperations {

	private final UppidyApi uppidyApi;
	
	public UserTemplate(UppidyApi uppidyApi, boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.uppidyApi = uppidyApi;
	}

	public ApiProfile getUserProfile() {
		requireAuthorization();
		return getUserProfile("me");
	}

	public ApiProfile getUserProfile(String userId) {
		return uppidyApi.fetchObject(userId + "/profile", ApiProfile.class, null);
	}
	
	public byte[] getUserProfileImage(String imageType) {
		requireAuthorization();
		return getUserProfileImage("me", imageType);
	}
	
	public byte[] getUserProfileImage(String userId, String imageType) {
		return uppidyApi.fetchImage(userId, "picture", imageType);
	}

	public List<ApiProfile> search(String query) {
		requireAuthorization();
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
		queryParams.add("query", query);
		return uppidyApi.fetchConnections("search", "user", ApiProfile.class, queryParams);
	}

}
