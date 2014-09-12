package com.forbesdigital.jee.oauth.spring.client.exceptions;

/**
 * Thrown to indicate that the grant type is not supported.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class UnsupportedGrantTypeException extends AbstractOAuthTokenRequestException {
	
	public UnsupportedGrantTypeException(String msg) {
		super(msg);
	}
	
	@Override
	public String getOAuth2ErrorCode() {
		return "unsupported_grant_type";
	}
}
