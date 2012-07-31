package com.uppidy.android.sdk.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.springframework.social.facebook.api.ImageType;
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
		return uppidyApi.fetchObject(userId + "/profile", ApiProfile.class);
	}
	
	public byte[] getUserProfileImage() {
		requireAuthorization();
		return getUserProfileImage("me", ImageType.NORMAL);
	}
	
	public byte[] getUserProfileImage(String userId) {
		return getUserProfileImage(userId, ImageType.NORMAL);
	}

	public byte[] getUserProfileImage(ImageType imageType) {
		requireAuthorization();
		return getUserProfileImage("me", imageType);
	}
	
	public byte[] getUserProfileImage(String userId, ImageType imageType) {
		return uppidyApi.fetchImage(userId, "picture", imageType);
	}

	public List<String> getUserPermissions() {
		JsonNode responseNode = uppidyApi.fetchObject("me/permissions", JsonNode.class);		
		return deserializePermissionsNodeToList(responseNode);
	}

	public List<ApiContactInfo> search(String query) {
		requireAuthorization();
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
		queryParams.add("query", query);
		return uppidyApi.fetchConnections("search", "user", ApiContactInfo.class, queryParams);
	}

	private List<String> deserializePermissionsNodeToList(JsonNode jsonNode) {
		JsonNode dataNode = jsonNode.get("data");			
		List<String> permissions = new ArrayList<String>();
		for (Iterator<JsonNode> elementIt = dataNode.getElements(); elementIt.hasNext(); ) {
			JsonNode permissionsElement = elementIt.next();
			for (Iterator<String> fieldNamesIt = permissionsElement.getFieldNames(); fieldNamesIt.hasNext(); ) {
				permissions.add(fieldNamesIt.next());
			}
		}			
		return permissions;
	}
}
