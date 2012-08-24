package com.uppidy.android.sdk.connect;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Uppidy-specific extension of OAuth2Template to allow password grant type
 * 
 * @author arudnev@uppidy.com
 */
public class UppidyOAuth2Template extends OAuth2Template {
	
	private final String clientId;
	private final String accessTokenUrl;

	public UppidyOAuth2Template(String clientId, String baseUrl) {
		super(clientId, "", baseUrl + "/oauth/authorize", baseUrl + "/oauth/dialog", baseUrl + "/oauth/token");
		this.clientId = clientId;
		this.accessTokenUrl = baseUrl + "/oauth/token";
	}

	public AccessGrant login(String username, String password, MultiValueMap<String, String> additionalParameters) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("client_id", clientId);
		params.set("username", username);
		params.set("password", password);
		params.set("grant_type", "password");
		if (additionalParameters != null) {
			params.putAll(additionalParameters);
		}
		return postForAccessGrant(accessTokenUrl, params);
	}
}
