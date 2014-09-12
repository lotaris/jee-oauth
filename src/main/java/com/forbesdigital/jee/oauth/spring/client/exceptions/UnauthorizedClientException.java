package com.forbesdigital.jee.oauth.spring.client.exceptions;

/**
 * Thrown to indicate that the client is not authorized to use a grant type.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class UnauthorizedClientException extends AbstractOAuthTokenRequestException {
	
	public UnauthorizedClientException(String msg) {
		super(msg);
	}

	@Override
	public int getHttpStatusCode() {
		// The spec says this can be unauthorized
		return 401;
	}
	
	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized_client";
	}
}
