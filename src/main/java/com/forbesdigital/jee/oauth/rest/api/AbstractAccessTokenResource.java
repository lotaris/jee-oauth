package com.forbesdigital.jee.oauth.rest.api;

import com.forbesdigital.jee.oauth.OAuthTokenError;
import com.forbesdigital.jee.oauth.OAuthTokenResponse;
import com.forbesdigital.jee.oauth.configuration.EOAuthGrantType;
import com.forbesdigital.jee.oauth.configuration.IOAuthConfiguration;
import com.forbesdigital.jee.oauth.configuration.OAuthContext;
import com.forbesdigital.jee.oauth.model.IOAuthClient;
import com.forbesdigital.jee.oauth.model.IOAuthToken;
import com.forbesdigital.jee.oauth.model.IOAuthUser;
import com.forbesdigital.jee.oauth.spring.client.exceptions.AbstractOAuthTokenRequestException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidGrantException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidRequestException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidScopeException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.core.Response;
import org.springframework.http.HttpStatus;

/**
 * Abstract implementation of a resource for requesting OAuth Tokens. 
 * Provides implementation for default OAuth specification and also abstract and protected methods 
 * which can/have to be overwritten in order to provide custom functionality to the resource. 
 * 
 * @param <Client> An class implementing IOAuthClient
 * @param <User> An class implementing IOAuthUser
 * @param <Token> An class implementing IOAuthToken
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public abstract class AbstractAccessTokenResource<Client extends IOAuthClient, User extends IOAuthUser, Token extends IOAuthToken> {
	
	// Header names
	private static final String CACHE_CONTROL = "Cache-Control";
	private static final String PRAGMA = "Pragma";
	// Header values
	private static final String CACHE_CONTROL_NO_STORE = "no-store";
	private static final String PRAGMA_NO_CACHE = "no-cache";
	
	/**
	 * A method for requesting an OAuth Token.
	 * 
	 * @param grantType The requested OAuth grant type
	 * @param scope The requested list of scopes, formatted as a space separated String
	 * @param username The provided OAuth User username
	 * @param password The provided OAuth User password
	 * @param expiresIn The provided value in seconds for the token lifetime
	 * @return The generated OAuth Token if the provided parameters were correct
	 */
	protected Response requestToken(String grantType, String scope, 
			String username, String password, String expiresIn){
		try {
			Client client = getAuthenticatedClient();
			
			User user = getAuthenticatedUser(grantType, username);

			Set<String> grantedScopes = validateOAuthScope(scope, client, grantType);
			
			// calculate the token lifetime and expirationDate
			Integer expiresInParsed = validateExpiresIn(expiresIn);
			
			Integer tokenLifetime = getTokenLifetime(client, expiresInParsed, user);
			Token token = createOAuthToken(client, tokenLifetime, grantedScopes, user);

			OAuthTokenResponse tokenTO = toTransferObject(token);
			Map<String, String> headers = new HashMap<>();
			headers.put(CACHE_CONTROL, CACHE_CONTROL_NO_STORE);
			headers.put(PRAGMA, PRAGMA_NO_CACHE);
			
			//return the generated access token and the Cache-Cntrol and Pragma headers.
			return buildSuccessfulResponse(tokenTO, headers);
			
		} catch (AbstractOAuthTokenRequestException cae) {
			OAuthTokenError errorTO = new OAuthTokenError(cae.getOAuth2ErrorCode(), cae.getMessage());
			return buildErrorResponse(errorTO, cae.getHttpStatusCode());
		}
	}

	/**
	 * @return The authenticated OAuth Client
	 */
	protected abstract Client getAuthenticatedClient();

	/**
	 * Retrieves the OAuth User with the provided username. 
	 * Should be overwritten if password grant type is used. Otherwise not. 
	 * 
	 * @param username The provided username
	 * @return The OAuth User, based on the provided username.
	 */
	protected User getOAuthUser(String username){
		throw new UnsupportedOperationException("This method has to be overwritten if password grant type is used.");
	}

	/**
	 * Creates an oAuth Token based on the provided information.
	 * 
	 * @param client The authenticated OAuth Client
	 * @param tokenLifetime The requested Token Lifetime
	 * @param grantedScopes The granted OAuth Scopes
	 * @param user The authenticated OAuth user, if there is one
	 * @return The generated Token
	 */
	protected abstract Token createOAuthToken(Client client, Integer tokenLifetime, Set<String> grantedScopes, User user);

	/**
	 * Method provided to be overwritten in case a custom processing of the granted scopes is desired after the default OAuth validation.
	 * 
	 * @param grantedScopes The Granted OAuth Scopes
	 * @return The processed list of Granted OAuth Scopes
	 */
	protected Set<String> afterOAuthScopesResolution(Set<String> grantedScopes){
		// Default behaviour - the granted scopes list is not modified
		return grantedScopes;
	}
	
	/**
	 * Method provided to be overwritten in case a custom processing of the token lifetime is desired after the default OAuth validation.
	 * 
	 * @param client The authenticated OAuth Client
	 * @param tokenLifetime The requested Token Lifetime
	 * @param user The authenticated OAuth user, if there is one
	 * @return The processed token lifetime
	 */
	protected Integer afterTokenLifetimeResolution(Client client, Integer tokenLifetime, User user) {
		// Default behaviour - the token lifetime is not modified
		return tokenLifetime;
	}

	/**
	 * Method provided to be overwritten in case additional information need to be added in the response body. 
	 * 
	 * @param token The OAuth Token
	 * @return A map containing the name and the value of the additional parameters
	 */
	protected  Map<String, Object> getAdditionalResponseParameters(Token token) {
		// Default behaviour - no additional information needed
		return null;
	}

	/**
	 * Builds a successful response with the provided token To as body and with the provided headers.
	 * 
	 * @param tokenResponse The token response containing all information about the generated OAuth Token
	 * @param headers The list of header to be added to the response
	 * @return The built response
	 */
	protected Response buildSuccessfulResponse(OAuthTokenResponse tokenResponse, Map<String, String> headers) {
		return buildResponse(tokenResponse, headers, HttpStatus.OK.value());
	}

	/**
	 * Builds an error response using the provided error to as body and with the provided status code.
	 * 
	 * @param errorResponse The error response
	 * @param statusCode The status code
	 * @return The built response
	 */
	protected Response buildErrorResponse(OAuthTokenError errorResponse, int statusCode) {
		return buildResponse(errorResponse, null, statusCode);
	}
	
	/**
	 * Builds a HTTP Response with the provided response body, headers and status code.
	 * 
	 * @param responseBody The response body
	 * @param headers The headers
	 * @param statusCode The status code
	 * @return The built response
	 */
	private Response buildResponse(Object responseBody, Map<String, String> headers, int statusCode) {
		// Constructs an HTTP 200 OK response with the specified entity as the body.
		Response.ResponseBuilder responseBuilder = Response.ok(responseBody);
		// Set the headers
		if (headers != null && !headers.isEmpty() ){
			for (String header : headers.keySet()) {
				responseBuilder.header(header, headers.get(header));
			}
		}
		// Set the status code
		responseBuilder.status(statusCode);
		
		return responseBuilder.build();
	}
	
	//<editor-fold defaultstate="collapsed" desc="Validations">
	/**
	 * @param grantType The requested grant type
	 * @param username The provided username
	 * @return The Authenticated OAuth User, if there is one. Otherwise null.
	 */
	private User getAuthenticatedUser(String grantTypeStr, String username){
		EOAuthGrantType grantType = EOAuthGrantType.fromValue(grantTypeStr);
		if (EOAuthGrantType.RESOURCE_OWNER_PASSWORD_CREDENTIALS.equals(grantType)) {
			User user = getOAuthUser(username);
			if (user == null) {
				throw new InvalidGrantException("The provided authorization grant is not valid.");
			}
			return user;
		}
		return null;
	}

	/**
	 * Validates the requested scope and builds the granted scope which will be associated to the token.
	 * 
	 * @param requestedScope The requested scope
	 * @param client The Authenticated Client
	 * @param grantType The requested grantType (a valid {@link EOAuthGrantType} string)
	 * @return The list of granted scopes
	 */
	private Set<String> validateOAuthScope(String requestedScope, Client client, String grantTypeStr) {
		
		Set<String> grantedScopes = new TreeSet<>();
		
		// In case no scope is requested return the default BASIC_ACCESS scope only.
		if (requestedScope == null || requestedScope.isEmpty()){
			// BASIC_SCOPE granted by default
			return afterOAuthScopesResolution(grantedScopes);
		}
			
		// Check Scope format
		if (!requestedScope.matches(Token.SCOPE_PATTERN)) {
			throw new InvalidScopeException("The requested scope is malformed.");
		}
		
		// Split Scope by space and validate each individual scope
		String[] requestedScopes = requestedScope.split(Token.SCOPES_SEPARATOR);
		Set<String> allScopes = OAuthContext.getConfig().getAllScopes();
		Set<String> allowedScopes = OAuthContext.getConfig().getClientRole(client.getClientRole()).getAllowedScopes();
		for (String clientScope : requestedScopes) {
			
			// Handle the case when double spaces are found in the requested Scope.
			if (clientScope.isEmpty()){
				continue;
			}
			// Check if the scope is valid
			if (!allScopes.contains(clientScope)) {
				throw new InvalidScopeException("The requested scope is invalid.");
			}
			// Check if the scope is allowed
			if (!allowedScopes.contains(clientScope)) {
				throw new InvalidScopeException("The requested scope exceeds the scope granted by the resource owner.");
			}
			
			// check that the scope is allowed for the given grant type
			EOAuthGrantType grantType = EOAuthGrantType.fromValue(grantTypeStr);
			if (!OAuthContext.getConfig().getAllowedScopes(grantType).contains(clientScope)) {
				throw new InvalidScopeException("The requested scope requires a different grant_type.");
			}
			
			// Add the scope to the granted scopes list
			grantedScopes.add(clientScope);
		}
		
		return afterOAuthScopesResolution(grantedScopes);
	}
	
	/**
	 * Calculates the token lifetime based on the required expiresIn, the client token lifetime 
	 * and the default token lifetime for the user role.
	 * 
	 * @param client The authenticated OAuth Client
	 * @param user The authenticated OAuth user, if there is one
	 * @param expiresIn The requested value in seconds for the token lifetime
	 * @return The calculated token lifetime.
	 */
	private Integer getTokenLifetime(Client client, Integer expiresIn, User user){
		Integer tokenLifetime = client.getTokenLifetime();
		if (tokenLifetime == null) {
			// Use default tokenLifetime in case this is not defined for the client
			IOAuthConfiguration config = OAuthContext.getConfig();
			tokenLifetime = config.getClientRole(client.getClientRole()).getTokenLifetime();
		}
		// Try setting the lifetime requested by the user
		if (expiresIn != null && expiresIn > 0) {
			tokenLifetime = Math.min(expiresIn, tokenLifetime);
		}
		return afterTokenLifetimeResolution(client, tokenLifetime, user);
	}

	/**
	 * Validates expiresIn parameter to be a valid strictly positive integer value.
	 * 
	 * @param expiresIn The requested value in seconds for the token lifetime
	 * @return the parsed expiresIn value.
	 */
	private Integer validateExpiresIn(String expiresIn) {
		if (expiresIn == null) {
			return null;
		}
		// try parse expires_in to a integer
		Integer expiresInParsed = null;
		try {
			expiresInParsed = Integer.parseInt(expiresIn);
		} catch (NumberFormatException e) {
			throw new InvalidRequestException("The 'expires_in' parameter is not a valid strictly positive integer value.");
		}
		if (expiresInParsed <= 0) {
			throw new InvalidRequestException("The 'expires_in' parameter is not a valid strictly positive integer value.");
		}
		return expiresInParsed;
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Transfer methods">
	/**
	 * Converts a OAuth Token to a transfer object.
	 *
	 * @param token The OAuth Token to convert to a transfer object
	 * @return A transfer object corresponding to the inputted token
	 */
	private OAuthTokenResponse toTransferObject(Token token) {
		OAuthTokenResponse tokenTO = new OAuthTokenResponse();
		tokenTO.setAccessToken(token.getAccessToken());
		tokenTO.setTokenType(token.getTokenType());
		tokenTO.setExpiresIn(token.getExpiresIn());
		tokenTO.setScope(buildScope(token.getScopes()));
		String dateStr = new DateHelper().convertToUtcString(token.getExpirationDate());
		tokenTO.setExpirationDate(dateStr);
		// Add additional information in the token TO
		Map<String, Object> additionalInfo = getAdditionalResponseParameters(token);
		if (additionalInfo != null && !additionalInfo.isEmpty()){
			tokenTO.getAdditionalInformation().putAll(additionalInfo);
		}
		return tokenTO;
	}

	/**
	 * Build a scope string containing all granted scopes, separate by one white space.
	 * 
	 * @param grantedScopes The granted scopes list
	 * @return The generated scope string.
	 */
	private String buildScope(Set<String> grantedScopes) {
		StringBuilder scopeBuilder = new StringBuilder();
		for (String scope : grantedScopes) {
			scopeBuilder.append(scope);
			scopeBuilder.append(Token.SCOPES_SEPARATOR);
		}
		return scopeBuilder.toString().trim();
	}
	//</editor-fold>

}
