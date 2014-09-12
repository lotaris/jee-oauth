package com.forbesdigital.jee.oauth.configuration;

import java.util.Set;

/**
 * An interface for an OAuth client role.
 * 
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public interface IOAuthClientRole {
	
	/**
	 * @return The name of this role
	 */
	public String getName();
	
	/**
	 * Get the scopes that this role grants access too. The set returned must contain a 
	 * subset (or all) of the scopes defined in the OAuth configuration for this 
	 * application. If a client with this role attempts to request a scope not contained
	 * in this set, it will get an error.
	 * 
	 * @return A set of OAuth scopes allowed for this client role
	 * @see IOAuthConfiguration#getOAuthScopes() 
	 */
	public Set<String> getAllowedScopes();
	
	/**
	 * Get the grant types that this role grants access too. The set returned must contain 
	 * a subset (or all) of the grant types supported by the OAuth library. If a client 
	 * with this role attempts to use a grant type not contained in this set, it will get 
	 * an error.
	 * 
	 * @return A set of OAuth grant types allowed for this client role
	 * @see EOAuthGrantType
	 */
	public Set<EOAuthGrantType> getAllowedOAuthGrantTypes();
	
	/**
	 * Get the maximum token lifetime that this role allows. The returned value represents 
	 * the maximum duration for an OAuth token requested by a client with this role. If a 
	 * client with this role requests a bigger lifetime or does not specify any lifetime,
	 * this value will be used.
	 * 
	 * @return The maximum (and default) OAuth token lifetime
	 */
	public Integer getTokenLifetime();
}
