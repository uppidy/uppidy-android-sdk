package com.uppidy.android.sdk.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.social.ApiException;
import org.springframework.social.MissingAuthorizationException;

/**
 * Interface defining operations that can be used to backup messages to Uppidy.
 * 
 * @author arudnev@uppidy.com
 */
public interface BackupOperations {

	List<ApiContainer> listContainers(Map<String, String> queryParams);

	ApiModifications saveContainer(ApiContainer data);

	void deleteContainer(String containerId);

	/**
	 * Saves contacts, conversations, messages and attachments.
	 * 
	 * @param containerId
	 *            id of backup container to sync to
	 * @param data
	 *            sync data
	 * @return {@link ApiModifications} with lists of created, updated or deleted entities
	 * @throws ApiException
	 *             if there is an error while communicating with Uppidy.
	 * @throws MissingAuthorizationException
	 *             if UppidyTemplate was not created with an access token.
	 */
	ApiModifications sync(String containerId, ApiSync data);
	
	List<ApiContact> listContacts(String containerId, Map<String, String> queryParams);

	List<ApiMessage> listMessages(String containerId, Map<String, String> queryParams);

	List<ApiConversation> listConversations(String containerId, Map<String, String> queryParams);
	
	ApiModifications upload(String containerId, List<ApiBodyPart> data);
	
	// List<ApiBodyPart> download(String containerId, List<ApiBodyPart> data);

	Date getFirstMessageSyncDate(String containerId);

	Date getLastMessageSyncDate(String containerId);
}
