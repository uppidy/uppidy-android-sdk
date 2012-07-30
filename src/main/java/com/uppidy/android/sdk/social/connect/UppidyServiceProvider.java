package com.uppidy.android.sdk.social.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

import com.uppidy.android.sdk.social.api.Uppidy;
import com.uppidy.android.sdk.social.api.impl.UppidyTemplate;

/**
 * Uppidy ServiceProvider implementation.
 * 
 * @author arudnev@uppidy.com
 */
public class UppidyServiceProvider extends AbstractOAuth2ServiceProvider<Uppidy> {
	private String baseUrl;

	public UppidyServiceProvider(String clientId, String clientSecret, String baseUrl) {
		super(new UppidyOAuth2Template(clientId, clientSecret, baseUrl));
		this.baseUrl = baseUrl;
	}

	public Uppidy getApi(String accessToken) {
		return new UppidyTemplate(baseUrl + "/rest/", accessToken);
	}
	
}