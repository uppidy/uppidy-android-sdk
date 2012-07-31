package com.uppidy.android.sdk.social.api;

import java.util.List;

/**
 * Interface defining operations that can be performed on a Uppidy user profiles.
 * 
 * @author arudnev@uppidy.com
 */
public interface UserOperations {
	
	/**
	 * Retrieves the profile for the authenticated user.
	 * @return the user's profile information.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	Profile getUserProfile();
	
	/**
	 * Retrieves the profile for the specified user.
	 * @param userId the Uppidy user ID to retrieve profile data for.
	 * @return the user's profile information.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 */
	Profile getUserProfile(String userId);

	/**
	 * Retrieves a list of permissions that the application has been granted for the authenticated user.
	 * @return the permissions granted for the user.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<String> getUserPermissions();
	
	/**
	 * Searches for users.
	 * @param query the search query (e.g., "Michael Scott")
	 * @return a list of {@link AppInfo}s, each representing a user who matched the given query.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<AppInfo> search(String query);
}
