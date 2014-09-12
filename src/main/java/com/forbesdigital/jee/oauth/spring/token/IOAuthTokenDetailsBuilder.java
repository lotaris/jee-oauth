package com.forbesdigital.jee.oauth.spring.token;

/**
 * Interface for a service used in {@link TokenDetailsService}. The token details builder
 * must be able to build an instance of {@link TokenDetails} given an access token.
 * 
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public interface IOAuthTokenDetailsBuilder {
	
	/**
	 * Build the token details for the given access token
	 *
	 * @param accessToken An access token
	 * @return The token details for Spring Security, null if not found
	 */
	OAuthTokenDetails buildTokenDetails(String accessToken);
}
