package com.uppidy.android.sdk.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import com.uppidy.android.sdk.api.ApiProfile;
import com.uppidy.android.sdk.api.Uppidy;

/**
 * Uppidy ApiAdapter implementation.
 * 
 * @author arudnev@uppidy.com
 */
public class UppidyAdapter implements ApiAdapter<Uppidy> {

	public boolean test(Uppidy uppidy) {
		try {
			uppidy.userOperations().getUserProfile();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	public void setConnectionValues(Uppidy uppidy, ConnectionValues values) {
		ApiProfile profile = uppidy.userOperations().getUserProfile();
		values.setProviderUserId(profile.getId());
		values.setDisplayName(profile.getUsername());
		values.setProfileUrl(uppidy.getBaseUrl() + profile.getId() + "/profile");
		values.setImageUrl(uppidy.getBaseUrl() + profile.getId() + "/picture");
	}

	public UserProfile fetchUserProfile(Uppidy uppidy) {
		ApiProfile profile = uppidy.userOperations().getUserProfile();
		return new UserProfileBuilder().setName(profile.getName()).setEmail(profile.getEmail()).setUsername(profile.getUsername()).build();
	}
	
	public void updateStatus(Uppidy uppidy, String message) {
		uppidy.feedOperations().updateStatus(message);
	}

}
