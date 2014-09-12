package com.forbesdigital.jee.oauth.spring.token.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown to indicate that the used Bearer token is expired.
 * 
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
public class ExpiredBearerTokenException extends AuthenticationException{

	public ExpiredBearerTokenException(String message) {
		super(message);
	}
}
