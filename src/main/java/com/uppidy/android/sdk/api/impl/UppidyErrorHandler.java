package com.uppidy.android.sdk.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.RevokedAuthorizationException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.uppidy.android.sdk.api.UppidyApiException;
import com.uppidy.android.sdk.api.UppidyApiValidationException;
import com.uppidy.android.sdk.api.UppidyApiVerificationException;
import com.uppidy.android.util.StringUtil;

/**
 * Subclass of {@link DefaultResponseErrorHandler} that handles errors from
 * Uppidy's API, interpreting them into appropriate exceptions.
 * 
 * @author arudnev@uppidy.com
 */
class UppidyErrorHandler extends DefaultResponseErrorHandler {

	public static final String HEADER_ERRORS = "U-Errors";
	public static final String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";

	public static final String ERROR_TYPE = "type";
	public static final String ERROR_CODE = "error";
	public static final String ERROR_MESSAGE = "error_description";

	public static final String ERROR_TYPE_OAUTH = "OAuthException";
	public static final String ERROR_TYPE_UPPIDY = "UppidyException";
	
	
	enum UppidyError
	{
		ACCOUNT_DISABLED("account.Disabled"),
		ACCOUNT_UNAUTHORIZED("account.Unauthorized");
		
		private String value;
		UppidyError( String value )
		{
			this.value = value;
		}
		
		private String getValue()
		{
			return value;
		}
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		Map<String, Object> errorDetails = extractErrorDetailsFromResponse(response);
		if (errorDetails == null) {
			handleUncategorizedError(response, errorDetails);
		}

		handleUppidyError(response.getStatusCode(), errorDetails);

		// if not otherwise handled, do default handling and wrap with
		// UncategorizedApiException
		handleUncategorizedError(response, errorDetails);
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		if (super.hasError(response)) {
			return true;
		}
		HttpHeaders headers = response.getHeaders();
		if (headers.containsKey(HEADER_ERRORS) || headers.containsKey(HEADER_WWW_AUTHENTICATE)) {
			return true;
		}
		return false;
	}

	/**
	 * Examines the error data returned from Uppidy and throws the most
	 * applicable exception.
	 * 
	 * Here is map of various oauth and uppidy specific error codes to the spring security
	 * exception class:
	 * 
	 * <table>
	 * <th>
	 * <td>HTTP Status</td>
	 * <td>Error Code</td>
	 * <td>Spring Security Exception</td>
	 * <td>Error Message</td></th>
	 * <tr>
	 * <td>401(Unauthorized)</td>
	 * <td>invalid_client</td>
	 * <td>InvalidClientException</td>
	 * <td>
	 * "A client id must be provided" <br />
	 * "Client not found: " + clientId <br />
	 * "Bad client credentials" <br />
	 * "Client ID mismatch" <br />
	 * "A client_id must be supplied."</td>
	 * </tr>
	 * <tr>
	 * <td>401(Unauthorized)</td>
	 * <td>unauthorized_client</td>
	 * <td>UnauthorizedClientException</td>
	 * <td>???</td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>invalid_grant</td>
	 * <td>InvalidGrantException</td>
	 * <td>"Invalid authorization code: " + authorizationCode <br />
	 * "An implicit grant could not be made" <br />
	 * "A client must have at least one authorized grant type." <br />
	 * "A redirect_uri can only be used by implicit or authorization_code grant types."
	 * <br />
	 * "Could not authenticate user: " + username <br />
	 * e.getMessage() where e is instance of BadCredentialsException thrown by
	 * authenticationManager.authenticate(userAuth) <br />
	 * "Unauthorized grant type: " + grantType <br />
	 * "Invalid refresh token: " + refreshTokenValue</td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>invalid_scope</td>
	 * <td>InvalidScopeException</td>
	 * <td>"Invalid scope: " + scope <br />
	 * "Unable to narrow the scope of the client authentication to " + scope +
	 * "."</td>
	 * </tr>
	 * <tr>
	 * <td>401(Unauthorized)</td>
	 * <td>invalid_token</td>
	 * <td>InvalidTokenException</td>
	 * <td>"Invalid token: " + token <br />
	 * "Invalid access token: " + tokenValue <br />
	 * "Invalid access token (no client id): " + tokenValue <br />
	 * "Invalid access token: " + accessTokenValue <br />
	 * "Access token expired: " + accessTokenValue <br />
	 * "Invalid refresh token (expired): " + refreshToken</td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>invalid_request</td>
	 * <td>InvalidRequestException</td>
	 * <td>
	 * "Possible CSRF detected - state parameter was present but no state could be found"
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>redirect_uri_mismatch</td>
	 * <td>RedirectMismatchException</td>
	 * <td>"Redirect URI mismatch." <br />
	 * "Invalid redirect: " + requestedRedirect +
	 * " does not match one of the registered values: " +
	 * redirectUris.toString()</td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>unsupported_grant_type</td>
	 * <td>UnsupportedGrantTypeException</td>
	 * <td>"Unsupported grant type: " + grantType</td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>unsupported_response_type</td>
	 * <td>UnsupportedResponseTypeException</td>
	 * <td>"Unsupported response types: " + responseTypes <br />
	 * "Unsupported response type: token"</td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>access_denied</td>
	 * <td>UserDeniedAuthorizationException</td>
	 * <td>"User denied access"</td>
	 * </tr>
	 * <tr>
	 * <td>400(Bad Request)</td>
	 * <td>???</td>
	 * <td>OAuth2Exception</td>
	 * <td>???</td>
	 * </tr>
	 * <tr>
	 * <td>401(Unauthorized)</td>
	 * <td>unauthorized</td>
	 * <td>UnauthorizedException</td>
	 * <td>e.getMessage() where e is top of stack trace that has
	 * AuthenticationException in it</td>
	 * </tr>
	 * <tr>
	 * <td>403(Forbidden)</td>
	 * <td>access_denied</td>
	 * <td>ForbiddenException</td>
	 * <td>e.getMessage() where e is top of stack trace that has
	 * AccessDeniedException in it</td>
	 * </tr>
	 * <tr>
	 * <td>403(Forbidden)</td>
	 * <td>access_denied</td>
	 * <td>OAuth2AccessDeniedException</td>
	 * <td>"Invalid token does not contain resource id (" + resourceId + ")"</td>
	 * </tr>
	 * <tr>
	 * <td>403(Forbidden)</td>
	 * <td>insufficient_scope</td>
	 * <td>InsufficientScopeException</td>
	 * <td>"Insufficient scope for this resource"</td>
	 * </tr>
	 * </table>
	 * 
	 * @param statusCode
	 *            http status code
	 * 
	 * @param errorDetails
	 *            a Map containing a "type", "code" and a "message"
	 *            corresponding to the Uppidy API's error response structure.
	 */
	void handleUppidyError(HttpStatus statusCode, Map<String, Object> errorDetails) {

		String type = (String) errorDetails.get(ERROR_TYPE);
		String code = (String) errorDetails.get(ERROR_CODE);
		String message = (String) errorDetails.get(ERROR_MESSAGE);

		if (message == null) message = getErrorMessage(code);
	
		if (type.equals(ERROR_TYPE_OAUTH)) 
		{  
			handleOauthError(statusCode, code, message );
		}
		else if (type.equals(ERROR_TYPE_UPPIDY)) 
		{ 
			handleUppidyError(statusCode, code, message );
		}
		else 
		{
			if (statusCode == HttpStatus.NOT_FOUND)
				throw new ResourceNotFoundException(message);
			else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) 
				throw new InternalServerErrorException(message);
		}
	}

	/*
	 * 400 invalid_grant 
	 * 	   "Unauthorized grant type: " -> OperationNotPermittedException
	 *     -> InvalidAuthorizationException
	 * 400 invalid_scope 
	 *     "Unable to narrow the scope" -> InsufficientPermissionException
	 *     -> InvalidAuthorizationException
	 * 400 invalid_request -> InvalidAuthorizationException
	 * 400 redirect_uri_mismatch -> InvalidAuthorizationException
	 * 400 unsupported_grant_type -> InvalidAuthorizationException
	 * 400 unsupported_response_type -> InvalidAuthorizationException
	 * 400 assess_denied -> RevokedAuthorizationException
	 * 400 ??? -> UncategorizedApiException
	 * 
	 * 401 unauthorized -> OperationNotPermittedException
	 * 401 invalid_client -> InvalidAuthorizationException
	 * 401 unauthorized_client -> OperationNotPermittedException
	 * 401 invalid_token
	 * 	   "..expired.." -> ExpiredAuthorizationException
	 * 	   "Invalid access token (no client id)" -> InvalidAuthorizationException 
	 *      -> RevokedAuthorizationException	
	 *      	 
	 * 403 assess_denied
	 *       "Invalid token does not contain resource id" -> InvalidAuthorizationException
	 *       -> OperationNotPermittedException
	 * 403 insufficient_scope -> InsufficientPermissionException
	 */
	private void handleOauthError(HttpStatus statusCode, String code, String message)
	{
		if (statusCode == HttpStatus.BAD_REQUEST)
		{
			if(code.equals("invalid_grant"))
			{
				if(message.startsWith("Unauthorized grant type:")) 
					throw new OperationNotPermittedException(message);
				else 
					throw new InvalidAuthorizationException(message); 
			}
			else if(code.equals("invalid_scope"))
			{
				if(message.startsWith("Unable to narrow the scope")) 
					throw new InsufficientPermissionException(message);
				else 
					throw new InvalidAuthorizationException(message); 
			}
			else if(code.equals("invalid_request")) 
				throw new InvalidAuthorizationException(message);
			else if(code.equals("redirect_uri_mismatch")) 
				throw new InvalidAuthorizationException(message);
			else if(code.equals("unsupported_grant_type")) 
				throw new InvalidAuthorizationException(message);
			else if(code.equals("unsupported_response_type")) 
				throw new InvalidAuthorizationException(message);
			else if(code.equals("assess_denied")) 
				throw new RevokedAuthorizationException(message);
			else
				throw new UncategorizedApiException(message, null);
		}
		
		else if(statusCode == HttpStatus.UNAUTHORIZED)
		{
			if(code.equals("unauthorized")) 
				throw new OperationNotPermittedException(message);
			else if(code.equals("invalid_client")) 
				throw new InvalidAuthorizationException(message);
			else if(code.equals("unauthorized_client")) 
				throw new InvalidAuthorizationException(message);
			else if(code.equals("invalid_token"))
			{
				if(message.startsWith("Invalid access token (no client id)")) 
					throw new InvalidAuthorizationException(message);
				else if(message.contains("expired")) 
					throw new ExpiredAuthorizationException();
				else 
					throw new RevokedAuthorizationException(message); 
			}			
		}

		else if(statusCode == HttpStatus.FORBIDDEN)
		{

			if(code.equals("assess_denied"))
			{
				if(message.startsWith("Invalid token does not contain resource id")) 
					throw new InvalidAuthorizationException(message);
				else
					throw new OperationNotPermittedException(message);
			}
			else if(code.equals("insufficient_scope")) 
				throw new InsufficientPermissionException(message);		
		}
	}
	
	/**
	 * <table>
	 * <tr><td>Code</td><td>Exception</td></tr>
	 * <tr><td>+account.Disabled</td><td>RevokedAuthorizationException</td></tr>
	 * <tr><td>+account.Unauthorized</td><td>InvalidAuthorizationException</td></tr>
	 * <tr><td>+account.ChangePassword</td><td>InvalidAuthorizationException</td></tr>
	 * <tr><td>+account.VerificationPending</td><td>UppidyApiVerificationException</td></tr>
	 * <tr><td>-account.NoEmpty</td><td>RevokedAuthorizationException</td></tr>
	 * <tr><td>+account.Forbidden</td><td>InvalidAuthorizationException</td></tr>
	 * 
	 * <tr><td>+device.Removed</td><td>what shall we do? re-create a container?</td></tr>
	 * <tr><td>+device.AccountDoesntMatch</td><td>?</td></tr>
	 * <tr><td>+device.AlreadyRegistered</td><td>?</td></tr>
	 * <tr><td>-device.NotEmpty</td><td>?</td></tr>
	 * <tr><td>-device.number.NotEmpty</td><td>?</td></tr>
	 * <tr><td>device.number.Length</td><td>?</td></tr>
	 * <tr><td>device.description.NotEmpty</td><td>?</td></tr>
	 * <tr><td>device.description.Length</td><td>?</td></tr>
	 * 
	 * <tr><td>+credentials.Rejected</td><td>?</td></tr>
	 * 
	 * <tr><td>host.unreachable</td><td>?</td></tr>
	 * 
	 * <tr><td>account.name.NotEmpty</td><td>?</td></tr>
	 * <tr><td>account.name.Length</td><td>?</td></tr>
	 * <tr><td></td><td></td></tr>
	 * <tr><td></td><td></td></tr>
	 * <tr><td></td><td></td></tr>
	 * <tr><td></td><td></td></tr>
	 *  
	 * <tr><td>none of the above listed and not empty</td><td>UppidyApiValidationException</td></tr>
	 * <tr><td></td><td></td></tr>
	 * </table>
	 * @param statusCode
	 * @param code
	 * @param message
	 */
	private void handleUppidyError(HttpStatus statusCode, String code, String message)
	{
		if (code == null || code.length()==0) return;
		
		if (code.equals("account.Disabled"))    
			throw new RevokedAuthorizationException(message); 
		else if (code.equals("account.Unauthorized"))
			throw new InvalidAuthorizationException(message);
		else if (code.equals("account.ChangePassword"))
			throw new InvalidAuthorizationException(message);
		else if (code.equals("account.VerificationPending"))
			throw new UppidyApiVerificationException(message);
		else if (code.equals("account.NoEmpty"))
			throw new RevokedAuthorizationException(message);
		else if (code.equals("account.Forbidden"))		
			throw new InvalidAuthorizationException( message );
		// none of the above listed codes
		else throw new UppidyApiValidationException(message);
	}
	
	private void handleInvalidAccessToken(String message) {
		if (message.contains("Session has expired at unix time")) {
			throw new ExpiredAuthorizationException();
		} else if (message.contains("The session has been invalidated because the user has changed the password.")) {
			throw new RevokedAuthorizationException(message);
		} else if (message.contains("The session is invalid because the user logged out.")) {
			throw new RevokedAuthorizationException(message);
		} else if (message.contains("has not authorized application")) {
			throw new RevokedAuthorizationException(message);
		} else {
			throw new InvalidAuthorizationException(message);
		}
	}

	private void handleUncategorizedError(ClientHttpResponse response, Map<String, Object> errorDetails) {
		try {
			super.handleError(response);
		} catch (Exception e) {
			String message = "No error details from Uppidy";
			if (errorDetails != null && errorDetails.containsKey(ERROR_MESSAGE)) {
				message = (String) errorDetails.get(ERROR_MESSAGE);
			}
			throw new UncategorizedApiException(message, e);
		}
	}

	private String getErrorMessage(String code) {
		return "Uppidy Error: " + code;
	}

	private Map<String, Object> extractFromErrorCodes(List<String> errorsHeader) {
		Map<String, Object> result = new HashMap<String, Object>();
		String code = errorsHeader.get(0);
		result.put(ERROR_TYPE, ERROR_TYPE_UPPIDY);
		result.put(ERROR_CODE, code);

		result.put(ERROR_MESSAGE, getErrorMessage(code));
		return result;
	}

	private Map<String, Object> extractErrors(List<String> errorsHeader, String prefix, String type) {
		for (String error : errorsHeader) {
			String[] pairs = StringUtil.splitIgnoringQuotes(error.substring(prefix.length()), ',');
			Map<String, Object> result = new HashMap<String, Object>();
			result.putAll(StringUtil.splitEachArrayElementAndCreateMap(pairs, "=", "\""));
			if (!result.containsKey(ERROR_TYPE)) {
				result.put(ERROR_TYPE, type);
			}
			return result;
		}
		return null;
	}

	/*
	 * Attempts to extract Uppidy error details from the response. Returns null
	 * if the response doesn't match the expected JSON error response.
	 */
	private Map<String, Object> extractErrorDetailsFromResponse(ClientHttpResponse response) throws IOException {
		List<String> errors = response.getHeaders().get(HEADER_ERRORS);
		if (errors != null) {
			// for now just return first error code
			return extractFromErrorCodes(errors);
			// TODO (AR): use format for the header similar to what is used by
			// OAuth
			// return extractErrors(errors, "");
		}

		ObjectMapper mapper = new ObjectMapper(new JsonFactory());
		String json = readFully(response.getBody());
		try {
			Map<String, Object> result = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
			String code = (String) result.get(ERROR_CODE);
			if (code != null && code instanceof String) {
				if (!result.containsKey(ERROR_TYPE)) {
					result.put(ERROR_TYPE, ERROR_TYPE_OAUTH);
				}
			}
			return result;
		} catch (JsonParseException e) {
			errors = response.getHeaders().get(HEADER_WWW_AUTHENTICATE);
			if (errors != null) {
				return extractErrors(errors, "Bearer", ERROR_TYPE_OAUTH);
			}
		}
		return null;
	}

	private String readFully(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		while (reader.ready()) {
			sb.append(reader.readLine());
		}
		return sb.toString();
	}
}
