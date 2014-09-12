package com.forbesdigital.jee.oauth.spring.token.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown to indicate that the Authentication header is missing or does not have the expected format.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class MissingAuthorizationHeaderException extends AuthenticationException {

	public MissingAuthorizationHeaderException(String message) {
		super(message);
	}
}
