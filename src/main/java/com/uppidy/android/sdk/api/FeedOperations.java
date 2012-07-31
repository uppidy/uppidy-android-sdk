package com.uppidy.android.sdk.api;

import java.util.List;

/**
 * Interface defining operations that can be performed on a Uppidy feed.
 * 
 * @author arudnev@uppidy.com
 */
public interface FeedOperations {

	/**
	 * Retrieves recent posts for the authenticated user.
	 * Requires "read_stream" permission to read non-public posts. 
	 * Returns up to the most recent 25 posts.
	 * @return a list of {@link ApiMessage}s for the authenticated user. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getFeed();

	/**
	 * Retrieves recent posts for the authenticated user.
	 * Requires "read_stream" permission to read non-public posts. 
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of {@link ApiMessage}s for the authenticated user. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getFeed(int offset, int limit);

	/**
	 * Retrieves recent feed entries for a given user. 
	 * Returns up to the most recent 25 posts.
	 * Requires "read_stream" permission to read non-public posts. 
	 * @param ownerId the Uppidy ID or alias for the owner (user, group, event, page, etc) of the feed.
	 * @return a list of {@link ApiMessage}s for the specified user. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getFeed(String ownerId);

	/**
	 * Retrieves recent feed entries for a given user. 
	 * Requires "read_stream" permission to read non-public posts.
	 * Returns up to the most recent 25 posts.
	 * @param ownerId the Uppidy ID or alias for the owner (user, group, event, page, etc) of the feed.
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of {@link ApiMessage}s for the specified user. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getFeed(String ownerId, int offset, int limit);

	/**
	 * Retrieves the user's home feed. This includes entries from the user's friends.
	 * Returns up to the most recent 25 posts.
	 * Requires "read_stream" permission. 
	 * @return a list of {@link ApiMessage}s from the authenticated user's home feed.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "read_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getHomeFeed();

	/**
	 * Retrieves the user's home feed. This includes entries from the user's friends.
	 * Requires "read_stream" permission. 
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of {@link ApiMessage}s from the authenticated user's home feed.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "read_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getHomeFeed(int offset, int limit);

	/**
	 * Retrieves a single post.
	 * @param entryId the entry ID
	 * @return the requested {@link ApiMessage}
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 */
	ApiMessage getPost(String entryId);
	
	/**
	 * Retrieves the post entries from the authenticated user's feed.
	 * Returns up to the most recent 25 posts.
	 * Requires "read_stream" permission. 
	 * @return a list of post {@link ApiMessage}s. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "read_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getPosts();

	/**
	 * Retrieves the post entries from the authenticated user's feed.
	 * Requires "read_stream" permission. 
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of post {@link ApiMessage}s. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "read_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getPosts(int offset, int limit);

	/**
	 * Retrieves the post entries from the specified owner's feed.
	 * Returns up to the most recent 25 posts.
	 * Requires "read_stream" permission. 
	 * @param ownerId the owner of the feed (could be a user, page, event, etc)
	 * @return a list of post {@link ApiMessage}s. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "read_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getPosts(String ownerId);

	/**
	 * Retrieves the post entries from the specified owner's feed.
	 * Requires "read_stream" permission. 
	 * @param ownerId the owner of the feed (could be a user, page, event, etc)
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of post {@link ApiMessage}s. 
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "read_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> getPosts(String ownerId, int offset, int limit);

	/**
	 * Posts a status update to the authenticated user's feed.
	 * Requires "publish_stream" permission.
	 * @param message the message to post.
	 * @return the ID of the new feed entry.
	 * @throws DuplicateStatusException if the status message duplicates a previously posted status.
	 * @throws RateLimitExceededException if the per-user/per-app rate limit is exceeded.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "publish_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	String updateStatus(String message);

	/**
	 * Posts a message to a feed.
	 * Requires "publish_stream" permission.
	 * @param ownerId the feed owner ID. Could be a user ID or a page ID.
	 * @param message the message to post.
	 * @return the id of the new feed entry.
	 * @throws DuplicateStatusException if the post duplicates a previous post.
	 * @throws RateLimitExceededException if the per-user/per-app rate limit is exceeded.
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "publish_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	String post(String ownerId, String message);

	/**
	 * Deletes a post.
	 * Requires "publish_stream" permission and the post must have been created by the same application.
	 * @param id the feed entry ID
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws InsufficientPermissionException if the user has not granted "publish_stream" permission.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	void deletePost(String id);

	/**
	 * Searches Uppidy's public feed.
	 * Returns up to 25 posts that match the query.
	 * @param query the search query (e.g., "Dr Seuss")
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 */
	List<ApiMessage> searchPublicFeed(String query);

	/**
	 * Searches Uppidy's public feed.
	 * @param query the search query (e.g., "Dr Seuss")
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 */
	List<ApiMessage> searchPublicFeed(String query, int offset, int limit);

	/**
	 * Searches the authenticated user's home feed.
	 * Returns up to 25 posts that match the query.
	 * @param query the search query (e.g., "Dr Seuss")
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> searchHomeFeed(String query);

	/**
	 * Searches the authenticated user's home feed.
	 * @param query the search query (e.g., "Dr Seuss")
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> searchHomeFeed(String query, int offset, int limit);

	/**
	 * Searches the authenticated user's feed.
	 * Returns up to 25 posts that match the query.
	 * @param query the search query (e.g., "football")
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> searchUserFeed(String query);

	/**
	 * Searches the authenticated user's feed.
	 * @param query the search query (e.g., "football")
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> searchUserFeed(String query, int offset, int limit);

	/**
	 * Searches a specified user's feed.
	 * Returns up to 25 posts that match the query.
	 * @param userId the ID of the user whose feed is to be searched
	 * @param query the search query (e.g., "football")
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> searchUserFeed(String userId, String query);

	/**
	 * Searches a specified user's feed.
	 * @param userId the ID of the user whose feed is to be searched
	 * @param query the search query (e.g., "football")
	 * @param offset the offset into the feed to start retrieving posts.
	 * @param limit the maximum number of posts to return.
	 * @return a list of {@link ApiMessage}s that match the search query
	 * @throws ApiException if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException if UppidyTemplate was not created with an access token.
	 */
	List<ApiMessage> searchUserFeed(String userId, String query, int offset, int limit);

}
