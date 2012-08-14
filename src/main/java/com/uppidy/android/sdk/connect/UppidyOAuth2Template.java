package com.uppidy.android.sdk.connect;

import java.util.Collections;

import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Uppidy-specific extension of OAuth2Template to use a RestTemplate that recognizes form-encoded responses as "text/plain".
 * Uppidy token responses are form-encoded results with a content type of "text/plain", which prevents the FormHttpMessageConverter
 * registered by default from parsing the results.
 * @author arudnev@uppidy.com
 */
public class UppidyOAuth2Template extends OAuth2Template {

	public UppidyOAuth2Template(String clientId, String baseUrl) {
		super(clientId, "", baseUrl + "/oauth/authorize", baseUrl + "/oauth/token");
	}

	/*
	@Override
	protected RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());
		FormHttpMessageConverter messageConverter = new FormHttpMessageConverter() {
			public boolean canRead(Class<?> clazz, MediaType mediaType) {
				// always read as x-www-url-formencoded even though Uppidy sets contentType to text/plain				
				return true;
			}
		};
		restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(messageConverter));
		return restTemplate;
	}
	
	@Override
	@SuppressWarnings("unchecked")	
	protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
		MultiValueMap<String, String> response = getRestTemplate().postForObject(accessTokenUrl, parameters, MultiValueMap.class);
		String expires = response.getFirst("expires");
		return new AccessGrant(response.getFirst("access_token"), null, null, expires != null ? Integer.valueOf(expires) : null);
	}
	*/
	/*
	public AccessGrant exchangeForAccess(String username, String password, MultiValueMap<String, String> additionalParameters) {
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
	*/
}
