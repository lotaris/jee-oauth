package com.forbesdigital.jee.oauth.spring.client;

/**
 * Interface for a service used in {@link ClientDetailsService}. The client details builder
 * must be able to build an instance of {@link ClientDetails} given a client id.
 * 
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public interface IOAuthClientDetailsBuilder {

	/**
	 * Builds the client details for the client with the given client id
	 *
	 * @param clientId The client_id of a Client
	 * @return The client details for Spring Security, null if not found
	 */
	OAuthClientDetails buildClientDetails(String clientId);
	
}
