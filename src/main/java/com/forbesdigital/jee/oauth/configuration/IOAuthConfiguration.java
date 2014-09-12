package com.forbesdigital.jee.oauth.configuration;

import java.util.Set;

/**
 * An interface for the OAuth library configuration class.
 * 
 * @see OAuthConfigurationListener
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public interface IOAuthConfiguration {

	/**
	 * @return All the OAuth scopes which can be used in this application
	 */
	Set<String> getAllScopes();
	
	/**
	 * @return All the client roles names which can be used in this application 
	 */
	Set<String> getClientRoleNames();
	
	/**
	 * Obtain a client role given its name.
	 * 
	 * @param name A client role name as returned by {@link IOAuthClientRole#getName()}
	 * @return A client role
	 */
	IOAuthClientRole getClientRole(String name);
	
	/**
	 * @return All the grant types accepted in this application
	 */
	Set<EOAuthGrantType> getAllGrantTypes();
	
	/**
	 * Get the scopes which can be requested for a given grant type. Make sure that your 
	 * configuration provides a set of scopes for each of {@link #getAllGrantTypes()}.
	 * 
	 * @param grantType A grant type
	 * @return A set of scopes allowed for this grant type
	 */
	Set<String> getAllowedScopes(EOAuthGrantType grantType);
}
