package com.forbesdigital.jee.oauth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines the list of allowed OAuth scopes for a REST resource method.
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @see OAuthScopeFilter
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyOAuthScopes {
	
	/**
	 * @return List of allowed OAuth scopes for the REST operation
	 */
	public String[] value();
}
