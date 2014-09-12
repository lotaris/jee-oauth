package com.forbesdigital.jee.oauth.spring.client.exceptions;

/**
 * Thrown to indicate that the request is invalid.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class InvalidRequestException extends AbstractOAuthTokenRequestException {
	
	public InvalidRequestException(String msg) {
		super(msg);
	}
	
	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_request";
	}
}
