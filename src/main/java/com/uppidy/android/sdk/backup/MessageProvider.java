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
	 * Iterator method, returns {@link ApiSync} object containing the next portion of {@link ApiMessage}s 
	 * and {@link ApiContact}s to be backed up.
	 * Implementor may return {@link ApiSync} with an empty list of messages or null to indicate 
	 * that there is nothing to backup.
	 * It seems a good idea to return a limited number of messages, especially MMS.
	 * @return - {@link ApiSync} object to be backed up
	 */
	public ApiSync getNextSyncBundle();
		
	/**
	 * Must return the container id used by Uppidy server to keep messages of the type provided by this {@link MessageProvider}.
	 * @return String - Uppidy container ID
	 */
	public String getContainerId();
	
	/**
	 * This method will be called by {@link BackupService} if and only if the last {@link ApiSync} object retrieved from
	 * {@code getNextSyncBundle()} is successfully backed up.
	 * Implementor must remember the messages backed up (remove them from database or mark as backed up) and 
	 * do not return those messages on subsequent {@code getNextSyncBundle()} calls. 
	 * @param sync - ApiSync object being backed up
	 */
	public void backupDone( ApiSync sync );	
	
}
