/**
 * Copyright (C) Uppidy Inc, 2012
 */
package com.uppidy.android.sdk.api;

/**
 * @author Vyacheslav Mukhortov
 *
 */
@SuppressWarnings("serial")
public class UppidyApiValidationException extends UppidyApiException
{
	public UppidyApiValidationException(String msg)
	{
		super(msg);
	}
	
	public UppidyApiValidationException(String msg, Throwable th)
	{
		super(msg, th);
	}
}
