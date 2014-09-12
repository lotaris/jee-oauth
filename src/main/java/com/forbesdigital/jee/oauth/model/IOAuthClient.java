package com.forbesdigital.jee.oauth.model;

/**
 * Interface for an OAuth Client. Defines the methods of the OAuth Client which are required by the OAuth lib. 
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public interface IOAuthClient {

	/**
	 * @return The client role
	 */
	String getClientRole();

	/**
	 * @return The default token lifetime for the client
	 */
	Integer getTokenLifetime();
	
}
