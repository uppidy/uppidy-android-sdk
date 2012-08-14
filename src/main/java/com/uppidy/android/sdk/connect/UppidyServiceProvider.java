package com.uppidy.android.sdk.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

import com.uppidy.android.sdk.api.Uppidy;
import com.uppidy.android.sdk.api.impl.UppidyTemplate;

/**
 * Uppidy ServiceProvider implementation.
 * 
 * @author arudnev@uppidy.com
 */
public class UppidyServiceProvider extends AbstractOAuth2ServiceProvider<Uppidy> {
	private String baseUrl;

	public UppidyServiceProvider(String clientId, String baseUrl) {
		super(new UppidyOAuth2Template(clientId, baseUrl));
		this.baseUrl = baseUrl;
	}

	public Uppidy getApi(String accessToken) {
		return new UppidyTemplate(baseUrl + "/rest/", accessToken);
	}
	
}