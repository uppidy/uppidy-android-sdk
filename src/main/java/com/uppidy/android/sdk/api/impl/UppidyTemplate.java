package com.uppidy.android.sdk.api.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.uppidy.android.sdk.api.BackupOperations;
import com.uppidy.android.sdk.api.FeedOperations;
import com.uppidy.android.sdk.api.Uppidy;
import com.uppidy.android.sdk.api.UppidyApi;
import com.uppidy.android.sdk.api.UserOperations;

/**
 * <p>This is the central class for interacting with Uppidy.</p>
 * <p>
 * There are some operations, such as searching, that do not require OAuth
 * authentication. In those cases, you may use a {@link UppidyTemplate} that is
 * created through the default constructor and without any OAuth details.
 * Attempts to perform secured operations through such an instance, however,
 * will result in {@link NotAuthorizedException} being thrown.
 * </p>
 * @author arudnev@uppidy.com
 */
public class UppidyTemplate extends AbstractOAuth2ApiBinding implements UppidyApi, Uppidy {
	
	/**
	 * Default Constant of the message to send in an exception when the host is unreachable
	 */
	private static final String HOST_UNREACHABLE = "host.unreachable";	
	
	private String baseUrl = "https://lab.uppidy.com/rest/";

	private UserOperations userOperations;
	
	private FeedOperations feedOperations;

	private BackupOperations backupOperations;

	private ObjectMapper objectMapper;

	/**
	 * Create a new instance of UppidyTemplate.
	 * This constructor creates a new UppidyTemplate able to perform unauthenticated operations against Uppidy's REST API.
	 * Some operations do not require OAuth authentication. 
	 * For example, retrieving a specified user's profile or feed does not require authentication (although the data returned will be limited to what is publicly available). 
	 * A UppidyTemplate created with this constructor will support those operations.
	 * Those operations requiring authentication will throw {@link NotAuthorizedException}.
	 */
	public UppidyTemplate(String baseUrl) {
		this.baseUrl = baseUrl;
		initialize();		
	}

	/**
	 * Create a new instance of UppidyTemplate.
	 * This constructor creates the UppidyTemplate using a given access token.
	 * @param accessToken An access token given by Uppidy after a successful OAuth 2 authentication (or through Uppidy's JS library).
	 */
	public UppidyTemplate(String baseUrl, String accessToken) {
		super(accessToken);
		this.baseUrl = baseUrl;
		initialize();
	}
	
	@Override
	public String getBaseUrl() {
		return baseUrl;
	}

	@Override
	public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
		// Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(requestFactory));
	}

	public UserOperations userOperations() {
		return userOperations;
	}
	
	public FeedOperations feedOperations() {
		return feedOperations;
	}
	
	public BackupOperations backupOperations() {
		return backupOperations;
	}
	
	public RestOperations restOperations() {
		return getRestTemplate();
	}
	
	// low-level REST API operations
	public <T> T fetchObject(String objectId, Class<T> type) {
		URI uri = URIBuilder.fromUri(baseUrl + objectId).build();
		try {
			return getRestTemplate().getForObject(uri, type);
		} catch (RestClientException e) {
			// If the exception has a SocketTimeoutException we throw a different message
			if (e.contains(SocketTimeoutException.class)) {
				throw new RestClientException(HOST_UNREACHABLE);
			}
			throw e;
		}
	}
	
	public <T> T fetchObject(String objectId, Class<T> type, MultiValueMap<String, String> queryParameters) {
		URI uri = URIBuilder.fromUri(baseUrl + objectId).queryParams(queryParameters).build();
		try {
			return getRestTemplate().getForObject(uri, type);
		} catch (RestClientException e) {
			// If the exception has a SocketTimeoutException we throw a different message
			if (e.contains(SocketTimeoutException.class)) {
				throw new RestClientException(HOST_UNREACHABLE);
			}
			throw e;
		}
	}

	public <T> List<T> fetchConnections(String objectId, String connectionType, Class<T> type, String... fields) {
		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<String, String>();
		if(fields.length > 0) {
			String joinedFields = join(fields);
			queryParameters.set("fields", joinedFields);
		}		
		return fetchConnections(objectId, connectionType, type, queryParameters);
	}

	public <T> List<T> fetchConnections(String objectId, String connectionType, Class<T> type, MultiValueMap<String, String> queryParameters) {
		String connectionPath = connectionType != null && connectionType.length() > 0 ? "/" + connectionType : "";
		URIBuilder uriBuilder = URIBuilder.fromUri(baseUrl + objectId + connectionPath).queryParams(queryParameters);
		JsonNode dataNode = getRestTemplate().getForObject(uriBuilder.build(), JsonNode.class);
		return deserializeDataList(dataNode, type);
	}

	public byte[] fetchImage(String objectId, String connectionType, String imageType) {
		URI uri = URIBuilder.fromUri(baseUrl + objectId + "/" + connectionType + "?type=" + imageType.toLowerCase()).build();
		ResponseEntity<byte[]> response = getRestTemplate().getForEntity(uri, byte[].class);
		if(response.getStatusCode() == HttpStatus.FOUND) {
			throw new UnsupportedOperationException("Attempt to fetch image resulted in a redirect which could not be followed. Add Apache HttpComponents HttpClient to the classpath " +
					"to be able to follow redirects.");
		}
		return response.getBody();
	}
	
	@SuppressWarnings("unchecked")
	public String publish(String objectId, String connectionType, Object data) {
		URI uri = URIBuilder.fromUri(baseUrl + objectId + "/" + connectionType).build();
		Map<String, Object> response = getRestTemplate().postForObject(uri, data, Map.class);
		return (String) response.get("id");
	}
	
	public <T> T publish(String objectId, String connectionType, Class<T> type, Object data) {
		URI uri = URIBuilder.fromUri(baseUrl + objectId + "/" + connectionType).build();
		return getRestTemplate().postForObject(uri, data, type);
	}
	
	public void post(String objectId, String connectionType, Object data) {
		URI uri = URIBuilder.fromUri(baseUrl + objectId + "/" + connectionType).build();
		getRestTemplate().postForObject(uri, data, String.class);
	}
	
	public <T> T post(String objectId, String connectionType, Object data, Class<T> responseType) {
		URI uri = URIBuilder.fromUri(baseUrl + objectId + "/" + connectionType).build();
		return getRestTemplate().postForObject(uri, data, responseType);
	}
	
	public void delete(String objectId) {
		LinkedMultiValueMap<String, String> deleteRequest = new LinkedMultiValueMap<String, String>();
		deleteRequest.set("method", "delete");
		URI uri = URIBuilder.fromUri(baseUrl + objectId).build();
		getRestTemplate().postForObject(uri, deleteRequest, String.class);
	}
	
	public void delete(String objectId, String connectionType) {
		LinkedMultiValueMap<String, String> deleteRequest = new LinkedMultiValueMap<String, String>();
		deleteRequest.set("method", "delete");
		URI uri = URIBuilder.fromUri(baseUrl + objectId + "/" + connectionType).build();
		getRestTemplate().postForObject(uri, deleteRequest, String.class);
	}
	
	// AbstractOAuth2ApiBinding hooks

	@Override
	protected void configureRestTemplate(RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new UppidyErrorHandler());
	}

	@Override
	protected MappingJacksonHttpMessageConverter getJsonMessageConverter() {
		MappingJacksonHttpMessageConverter converter = super.getJsonMessageConverter();
		objectMapper = new ObjectMapper();				
		converter.setObjectMapper(objectMapper);		
		return converter;
	}
	
	// private helpers
	private void initialize() {
		// Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory()));
		initSubApis();
	}
		
	private void initSubApis() {
		userOperations = new UserTemplate(this, isAuthorized());
		feedOperations = new FeedTemplate(this, isAuthorized());
		backupOperations = new BackupTemplate(this, isAuthorized());
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> deserializeDataList(JsonNode jsonNode, final Class<T> elementType) {
		try {
			CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, elementType);
			return (List<T>) objectMapper.readValue(jsonNode, listType);
		} catch (IOException e) {
			throw new UncategorizedApiException("Error deserializing data from Uppidy: " + e.getMessage(), e);
		}
	}
	
	private String join(String[] strings) {
		StringBuilder builder = new StringBuilder();
		if(strings.length > 0) {
			builder.append(strings[0]);
			for (int i = 1; i < strings.length; i++) {
				builder.append("," + strings[i]);
			}
		}
		return builder.toString();
	}
	
}
