/**
 * Copyright (C) Uppidy Inc, 2012
 */
package com.uppidy.android.sdk.backup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Vyacheslav Mukhortov
 * Listens for ConnectivityManager.CONNECTIVITY_ACTION intent action, checks the network state 
 * and starts {@link BackupService} if data network is available.
 * Enable this {@code <receiver>} in AndroidManifest.xml if you want to start 
 * {@link BackupService} when data network is available.
 * Requires the permission ACCESS_NETWORK_STATE. 
 */
public class NetworkStateListener extends BroadcastReceiver
{
	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if( action.equals( ConnectivityManager.CONNECTIVITY_ACTION ) )
		{
			ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if( networkInfo != null && networkInfo.isConnected() )
		    {
		    	context.startService(new Intent(BackupService.ACTION_BACKUP_ALL) );
		    }
		}
	}
}
