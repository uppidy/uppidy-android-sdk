/**
 * Copyright (C) Uppidy Inc, 2012
 */
package com.uppidy.android.sdk.backup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.content.LocalBroadcastManager;

/**
 * @author Vyacheslav Mukhortov
 * Listens for ConnectivityManager.CONNECTIVITY_ACTION, checks the network state 
 * and sends an intent to BackupService if data network is available.
 * Enable this {@code <receiver>} in AndroidManifest.xml if you want to start BackupService implicitly.
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
		    	LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BackupService.ACTION_START) );
		    }
		}
	}
}
