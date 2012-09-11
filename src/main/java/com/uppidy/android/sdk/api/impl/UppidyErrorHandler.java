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

		if (message == null) {
			message = getErrorMessage(code);
		}

		if (type.equals(ERROR_TYPE_OAUTH)) {
			// TODO (AR): handle oauth errors
		}

		if (statusCode == HttpStatus.OK) {
			if (message.contains("Some of the aliases you requested do not exist")) {
				throw new ResourceNotFoundException(message);
			}
		} else if (statusCode == HttpStatus.BAD_REQUEST) {
			if (message.contains("Unknown path components")) {
				throw new ResourceNotFoundException(message);
			} else if (message.equals("An access token is required to request this resource.")) {
				throw new MissingAuthorizationException();
			} else if (message
					.equals("An active access token must be used to query information about the current user.")) {
				throw new MissingAuthorizationException();
			} else if (message.startsWith("Error validating access token")) {
				handleInvalidAccessToken(message);
			} else if (message.equals("Error validating application.")) {
				// Access token with incorrect app ID
				throw new InvalidAuthorizationException(message);
			} else if (message.equals("Invalid access token signature.")) {
				// Access token that fails signature validation
				throw new InvalidAuthorizationException(message);
			} else if (message.contains("Application does not have the capability to make this API call.")
					|| message.contains("App must be on whitelist")) {
				throw new OperationNotPermittedException(message);
			} else if (message.contains("Invalid id") || message.contains("The parameter url is required")) {
				throw new OperationNotPermittedException("Invalid object for this operation");
			} else if (message.contains("Duplicate status message")) {
				throw new DuplicateStatusException(message);
			} else if (message.contains("Feed action request limit reached")) {
				throw new RateLimitExceededException();
			} else if (message
					.contains("The status you are trying to publish is a duplicate of, or too similar to, one that we recently posted")) {
				throw new DuplicateStatusException(message);
			}
		} else if (statusCode == HttpStatus.UNAUTHORIZED) {
			if (message.startsWith("Invalid access token")) {
				handleInvalidAccessToken(message);
			}
			throw new NotAuthorizedException(message);
		} else if (statusCode == HttpStatus.FORBIDDEN) {
			if (message.contains("Requires extended permission")) {
				String requiredPermission = message.split(": ")[1];
				throw new InsufficientPermissionException(requiredPermission);
			} else if (message.contains("Permissions error")) {
				throw new InsufficientPermissionException();
			} else if (message.contains("The user hasn't authorized the application to perform this action")) {
				throw new InsufficientPermissionException();
			} else {
				throw new OperationNotPermittedException(message);
			}
		} else if (statusCode == HttpStatus.NOT_FOUND) {
			throw new ResourceNotFoundException(message);
		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new InternalServerErrorException(message);
		}
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
