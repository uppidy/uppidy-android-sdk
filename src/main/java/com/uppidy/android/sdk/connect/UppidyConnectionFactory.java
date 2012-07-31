package com.uppidy.android.sdk.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import com.uppidy.android.sdk.api.Uppidy;

/**
 * Uppidy ConnectionFactory implementation.
 * @author arudnev@uppidy.com
 */
public class UppidyConnectionFactory extends OAuth2ConnectionFactory<Uppidy> {

	public UppidyConnectionFactory(String clientId, String clientSecret, String baseUrl) {
		super("uppidy", new UppidyServiceProvider(clientId, clientSecret, baseUrl), new UppidyAdapter());
	}
	
}
