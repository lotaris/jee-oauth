package com.forbesdigital.jee.oauth.spring.token.exceptions;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Thrown when the access to a resource is denied, typically due to missing scopes.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class OAuthAccessDeniedException extends IOException {

	private Set<String> scopes = new TreeSet<>();
	private String clientRole;

	public OAuthAccessDeniedException() {
		super();
	}

	public OAuthAccessDeniedException(Set<String> expectedScopes, String role) {
		super();
		this.scopes = expectedScopes;
		this.clientRole = role;
	}

	public Set<String> getExpectedScopes() {
		return scopes;
	}

	public String getRole() {
		return clientRole;
	}
}
