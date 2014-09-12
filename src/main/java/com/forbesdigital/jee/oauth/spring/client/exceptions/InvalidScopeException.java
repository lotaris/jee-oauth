package com.forbesdigital.jee.oauth.spring.client.exceptions;

/**
 * Thrown to indicate that one of the requested scopes is invalid.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class InvalidScopeException extends AbstractOAuthTokenRequestException {
	
	public InvalidScopeException(String msg) {
		super(msg);
	}
	
	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_scope";
	}
}
