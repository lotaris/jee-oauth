package com.forbesdigital.jee.oauth.spring.token.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown to indicate that the used Bearer token is malformed.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
public class MalformedBearerTokenException extends AuthenticationException{
	
	public MalformedBearerTokenException(String message) {
		super(message);
	}
}
