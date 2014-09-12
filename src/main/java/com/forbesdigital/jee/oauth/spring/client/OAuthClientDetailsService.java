package com.forbesdigital.jee.oauth.spring.client;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Represent the Spring Security UserDetails service for the clients
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class OAuthClientDetailsService implements UserDetailsService {

	private IOAuthClientDetailsBuilder builder;

	@Override
	public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException, DataAccessException {
		OAuthClientDetails details = builder.buildClientDetails(clientId);

		if (details == null) {
			throw new UsernameNotFoundException("Client not found.");
		}
		return details;
	}

	/**
	 * @param builder Service to build a {@link ClientDetails} instance from a client id
	 */
	public void setClientDetailsBuilder(IOAuthClientDetailsBuilder builder) {
		this.builder = builder;
	}
	
}
