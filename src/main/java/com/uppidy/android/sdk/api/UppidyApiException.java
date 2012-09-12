/**
 * Copyright (C) Uppidy Inc, 2012
 */
package com.uppidy.android.sdk.api;

/**
 * @author Vyacheslav Mukhortov
 *
 */
@SuppressWarnings("serial")
public class UppidyApiException extends RuntimeException
{	
	public UppidyApiException( String msg )
	{
		super(msg);
	}
	
	public UppidyApiException( String msg, Throwable th )
	{
		super( msg, th);
	}
}
