package com.uppidy.android.sdk.api;

import java.util.Date;
import java.util.List;

import org.springframework.social.ApiException;
import org.springframework.social.MissingAuthorizationException;

/**
 * Interface defining operations that can be performed on Uppidy feeds.
 * 
 * @author arudnev@uppidy.com
 */
public interface FeedOperations {
	
	/**
	 * Posts a status update to the authenticated user's feed.
	 * Requires "publish_stream" permission.
	 * @param message the message to post.
	 * @return the id of the new feed entry.
	 * @throws DuplicateStatusException if the status message duplicates a previously posted status.
	 * @throws RateLimitExceededException if the per-user/per-app rate limit is exceeded.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "publish_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	String updateStatus(String message);

	/**
	 * Searches a specified user's feed.
	 * @param contextType the type of the context to be searched, one of "account", "device", "contact", "group"
	 * @param contextId the id of the context whose feed is to be searched
	 * @param query the search query (e.g., "football")
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @param since the start date / time of the time range to search in.
	 * @param until the end date / time of the time range to search in.
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> searchFeed(String contextType, String contextId, String query, int offset, int limit, Date since, Date until);

}
