package com.forbesdigital.jee.oauth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines the list of required OAuth scopes for a REST resource method.
 * 
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 * @see OAuthScopeFilter
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AllOAuthScopes {
	
	/**
	 * @return List of required OAuth scopes for the REST operation
	 */
	public String[] value();
}
