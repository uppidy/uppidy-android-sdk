package com.uppidy.android.sdk.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface defining operations that can be used to backup messages to Uppidy.
 * 
 * @author arudnev@uppidy.com
 */
public interface BackupOperations {
	List<ApiContainer> listContainers(Map<String, String> queryParams);
	ApiContainer createContainer(Map<String, Object> parameters);
	ApiContainer updateContainer(String containerId, Map<String, Object> parameters);
	void deleteContainer(String containerId);
	
	void sync(String containerId, ApiSync data);

	List<ApiContact> listContacts(String containerId,  Map<String, String> queryParams);
	List<ApiMessage> listMessages(String containerId,  Map<String, String> queryParams);
	List<ApiConversation> listConversations(String containerId, Map<String, String> queryParams);

	Date getFirstMessageSyncDate(String containerId);
	Date getLastMessageSyncDate(String containerId);
}
