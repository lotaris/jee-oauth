package com.forbesdigital.jee.oauth.spring.user;

/**
 * Interface for a service used in {@link OAuthUserDetailsService}. The user details builder
 * must be able to build an instance of {@link OAuthUserDetails} given a username.
 * 
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public interface IOAuthUserDetailsBuilder {

	/**
	 * Builds the user details for the user with the given username
	 *
	 * @param username The username of a User
	 * @return The User details for Spring Security, null if not found
	 */
	OAuthUserDetails buildUserDetails(String username);
	
}
