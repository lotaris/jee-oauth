package com.forbesdigital.jee.oauth.spring.client.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Abstract exception which will be extended by all exceptions which can be thrown while requesting an OAuth Token.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public abstract class AbstractOAuthTokenRequestException extends AuthenticationException {
	
	public AbstractOAuthTokenRequestException(String msg) {
		super(msg);
	}
	
	/**
	 * @return The HTTP status code which should be set on the response.
	 */
	public int getHttpStatusCode() {
		// The spec says this is a bad request (not unauthorized)
		return 400;
	}

	/**
	 * @return The OAuth2 error code that should be returned in the response.
	 */
	public abstract String getOAuth2ErrorCode();
}