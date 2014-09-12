package com.forbesdigital.jee.oauth.spring.client;

import com.forbesdigital.jee.oauth.spring.AbstractExceptionTranslationFilter;
import com.forbesdigital.jee.oauth.OAuthTokenError;
import com.forbesdigital.jee.oauth.JsonObjectSerializer;
import com.forbesdigital.jee.oauth.spring.client.exceptions.AbstractOAuthTokenRequestException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

/**
 * Specific implementation of Exception Translation filter for Client Authentication.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class ClientExceptionTranslationFilter extends AbstractExceptionTranslationFilter {
	
	protected final JsonObjectSerializer jsonSerializer = new JsonObjectSerializer();

	@Override
	protected void handleSpringSecurityException(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		RuntimeException exception) throws IOException, ServletException {

		// There are only two types of exception that we can get here: an AccessDeniedException or an AuthenticationException

		if (exception instanceof AbstractOAuthTokenRequestException) {
			sendUnauthorizedAccessResponse(response, (AbstractOAuthTokenRequestException) exception);
		} else if (exception instanceof AuthenticationException) {
			sendUnauthorizedAccessResponse(response);
		}
	}

	/**
	 * Builds the error response in case of an Authentication Exception thrown by request OAuth Token API call authorization.
	 * The returned response content is in JSON and has the standard format specified by OAauth2 specifications.
	 * 
	 * @param response The response
	 */
	private void sendUnauthorizedAccessResponse(HttpServletResponse response) throws IOException {		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getOutputStream().print(buildErrorResponse(new OAuthTokenError(
				"invalid_client", 
				"You are not allowed to access this resource.")));
	}

	/**
	 * Builds the error response in case of an Authentication Exception thrown by request OAuth Token API call authorization.
	 * The returned response content is in JSON and has the standard format specified by OAauth2 specifications.
	 * 
	 * @param response The response
	 * @param tre An OAuth Token request exception
	 */
	private void sendUnauthorizedAccessResponse(HttpServletResponse response, AbstractOAuthTokenRequestException tre) throws IOException {
		response.setStatus(tre.getHttpStatusCode());
		response.setContentType("application/json");
		response.getOutputStream().print(buildErrorResponse(new OAuthTokenError(
				tre.getOAuth2ErrorCode(), 
				tre.getMessage())));
	}
	
	/**
	 * Builds a JSON error response based on the provided transfer object.
	 *
	 * @param response holds error data
	 * @return a JSON representation of the provided TO
	 */
	private String buildErrorResponse(OAuthTokenError response) {
		try {
			return jsonSerializer.writeValueAsString(response);
		} catch (IOException ex) {
			// nothing to do here
		}
		return null;
	}
	
}
