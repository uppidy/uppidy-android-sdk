package com.uppidy.android.sdk.social.api;

import org.springframework.social.ApiBinding;
import org.springframework.web.client.RestOperations;


public interface Uppidy extends ApiBinding {

	/**
	 * API for performing operations on Uppidy user profiles.
	 */
	UserOperations userOperations();
	
	/**
	 * API for performing operations on Uppidy feeds.
	 */
	FeedOperations feedOperations();
	
	/**
	 * API for performing backup operations.
	 */
	BackupOperations backupOperations();

	/**
	 * Returns the underlying {@link RestOperations} object allowing for
	 * consumption of Uppidy endpoints that may not be otherwise covered by the
	 * API binding. The RestOperations object returned is configured to include
	 * an OAuth 2 "Authorization" header on all requests.
	 */
	RestOperations restOperations();
	
	/**
	 * Base REST API URL
	 */
	String getBaseUrl();
	
}
