package com.forbesdigital.jee.oauth.spring.client.exceptions;

/**
 * Thrown to indicate that the grant is invalid.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class InvalidGrantException extends AbstractOAuthTokenRequestException {
	
	public InvalidGrantException(String msg) {
		super(msg);
	}
	
	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_grant";
	}
}
