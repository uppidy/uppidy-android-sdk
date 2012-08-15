/**
 * Copyright (C) Uppidy Inc, 2012
 */
package com.uppidy.android.sdk.backup;

import com.uppidy.android.sdk.api.ApiSync;

/**
 * @author Vyacheslav Mukhortov
 * Interface class for message providers used by {@link BackupService} to retrieve the messages to be backed up
 */
public interface MessageProvider
{
	/**
	 * Iterator method, returns the next portion of {@code Message}s to be backed up.
	 * Implementor may return {@link ApiSync} with an empty list of messages or null to indicate 
	 * that there is nothing to backup.
	 * It seems a good idea to return a limited number of messages, especially MMS.
	 * @return - the list of {@code Message}s to be backed up
	 */
	public ApiSync getNextSyncBundle();
		
	/**
	 * Must return the container id used by Uppidy server to keep messages of the type provided by this {@link MessageProvider}.
	 * @return String - Uppidy container ID
	 */
	public String getContainerId();
	
	/**
	 * This method will be called by {@link BackupService} if and only if the list of messages is successfully backed up.
	 * Implementor must remember the messages backed up (remove them from database or mark as backed up) and 
	 * do not return those messages on subsequent {@code getNextSyncBundle()} calls. 
	 * @param posts - messages being backed up
	 */
	public void backupDone( ApiSync sync );	
	
}
