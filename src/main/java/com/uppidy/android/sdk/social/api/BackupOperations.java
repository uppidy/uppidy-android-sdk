package com.uppidy.android.sdk.social.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface defining operations that can be used to backup messages to Uppidy.
 * 
 * @author arudnev@uppidy.com
 */
public interface BackupOperations {
	List<Container> listContainers(Map<String, String> queryParams);
	Container createContainer(Map<String, Object> parameters);
	Container updateContainer(String containerId, Map<String, Object> parameters);
	void deleteContainer(String containerId);
	
	void sync(String containerId, Sync data);

	List<Contact> listContacts(String containerId,  Map<String, String> queryParams);
	List<Message> listMessages(String containerId,  Map<String, String> queryParams);
	List<Conversation> listConversations(String containerId, Map<String, String> queryParams);

	Date getFirstMessageSyncDate(String containerId);
	Date getLastMessageSyncDate(String containerId);
}
