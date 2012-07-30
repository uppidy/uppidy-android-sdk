/**
 * Copyright (C) Uppidy Inc, 2012
 */
package com.uppidy.android.sdk.backup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.social.connect.ConnectionRepository;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.uppidy.android.sdk.social.api.BackupOperations;
import com.uppidy.android.sdk.social.api.Message;
import com.uppidy.android.sdk.social.api.Reference;
import com.uppidy.android.sdk.social.api.Sync;
import com.uppidy.android.sdk.social.api.Uppidy;

import de.akquinet.android.androlog.Log;

/**
 * @author Vyacheslav Mukhortov
 * There are 2 ways to initiate the backup service:
 * <ul>
 *   <li> by sending an intent with user-defined action used while registering {@link MessageProvider}
 *   <li> by calling Context.startService with an intent action {@link BackupService.ACTION_START}
 *   <>
 * </ul>
 * Requires the following permissions: ACCESS_NETWORK_STATE, WRITE_EXTERNAL_STORAGE
 */
public abstract class BackupService extends IntentService
{	
	protected static String TAG = "BackupService";
	public static final String ACTION_START = "com.uppidy.android.sdk.backup.START";
	public static final String ACTION_STOP  = "com.uppidy.android.sdk.backup.STOP";
	public static final String ENABLED      = "com.uppidy.android.sdk.backup.ENABLED";
	
	private HashMap<String,MessageProvider> providers = new HashMap<String, MessageProvider>();
	
	/**
	 * @param serviceName service name
	 */
	public BackupService(String serviceName)
	{
		super(serviceName);
	}

	/**
	 * Adds a new {@link MessageProvider} and associates it with an {@link Intent} {@code action}. 
	 * This method is thread-safe.
	 * @param action - {@link Intent} action to associate with
	 * @param mp - {@link MessageProvider} implementation
	 */
	public synchronized void addMessageProvider( String action, MessageProvider mp )
	{
		providers.put( action, mp );
	}

	/**
	 * Removes {@link MessageProvider} associated with {@code action} passed
	 * @param action
	 */
	public synchronized void removeMessageProvider( String action )
	{
		providers.remove( action );
	}
	
	protected Collection<MessageProvider> getMessageProviders()
	{
		return providers.values();
	}
	
	/**
	 * Returns {@link MessageProvider} associated with an {@code action}
	 * @param action
	 * @return {@link MessageProvider} or null if not found
	 */
	public synchronized MessageProvider getMessageProvider( String action )
	{
		return providers.get(action);
	}
	
	/**
	 * Must return Uppidy ConnectionRepository 
	 * @return ConnectionRepository - Uppidy ConnectionRepository
	 */
	protected abstract ConnectionRepository getUppidyConnectionRepository();
	
	/**
	 * Must return {@link SharedPreferences} object that will be used by BackupService 
	 * to keep a persistent state of the {@link BackupService.ENABLED} parameter. 
	 * @return
	 */
	protected abstract SharedPreferences getSharedPreferences();
	
	/**
	 * Handles 3 types of intents:
	 * <ul>
	 *   <li> BackupService.ACTION_START - starts backup if network is available
	 *   <li> BackupService.ACTION_STOP  - stops itself
	 *   <li> user defined intents passed while registering {@link MessageProvider}s
	 * </ul>
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent)
	{
		try
		{
			String action = intent.getAction();
			MessageProvider mp;
			if( action.equals( ACTION_START ) ) 
	    	{
				Editor editor = getSharedPreferences().edit();
				editor.putBoolean(ENABLED, true);
				editor.commit();
				backupAll(); 
	    	}
			else if( action.equals( ACTION_STOP  ) )
			{
				Editor editor = getSharedPreferences().edit();
				editor.putBoolean(ENABLED, false);
				editor.commit();
				stopSelf();
			}
			// user-defined intents     
			else if( (mp = providers.get(action)) != null ) 
			{
				if( getSharedPreferences().getBoolean(ENABLED, false) )
				{
					Log.w( TAG, "User defined intent (" + action + ") received but won't be processed because the service is not enabled."
							+ " Send an intent with action BackupService.ACTION_START first." );
				}
				else 
				{
					Log.w( TAG, "User defined intent (" + action + ") received, starting backup on container identified by " + mp.getContainerId() );
					while( doBackup( mp ) );
					Log.w( TAG, "Backup complete" );
				}
			}
		} 
		catch( Exception ex )
		{
			String msg = ex.getMessage();
			Log.e( msg == null ? ex.toString() : msg );
		}
	}

	private void backupAll()
	{
		if( !isOnline() ) return;
		// keep synchronizing portion by portion until there is nothing to do
		boolean somethingDone;
		do
		{
			somethingDone = false;
			for( MessageProvider mp : providers.values() )
			{
				somethingDone |= doBackup( mp );
			}
		} while( somethingDone );
	}
	
	private boolean doBackup( MessageProvider mp )
	{
		if( mp == null || !isOnline() ) return false;
		List<Message> messages = mp.getNextSyncBundle();
		if( messages == null || messages.size() == 0 ) return false;
		
		Uppidy uppidy = getUppidyConnectionRepository().findPrimaryConnection(Uppidy.class).getApi();
		if( uppidy == null )
		{
			Log.e( TAG, "Uppidy connection not found" );
			return false;
		}
		List<String> messageIds = new ArrayList<String>();
		for( Message m : messages ) 
		{
			messageIds.add( m.getFrom().getId() );
			for( Reference toRef : m.getTo() ) messageIds.add( toRef.getId() );
		}
		BackupOperations backupOperations = uppidy.backupOperations();
		
		Sync sync = new Sync();
		sync.setContacts(mp.getContacts(messageIds));
		sync.setMessages(messages);
		backupOperations.sync( mp.getContainerId(), sync );
		
		mp.backupDone( messages );
		return true;
	}
	
	private boolean isOnline() 
	{
	    ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	} 
}
