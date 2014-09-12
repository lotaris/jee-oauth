package com.forbesdigital.jee.oauth.spring.client.exceptions;

/**
 * Thrown to indicate that the client authentication failed.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class InvalidClientException extends AbstractOAuthTokenRequestException {
	
	public InvalidClientException(String msg) {
		super(msg);
	}

	@Override
	public int getHttpStatusCode() {
		// The spec says this can be unauthorized
		return 401;
	}
	
	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_client";
	}
}
