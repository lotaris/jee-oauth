package com.forbesdigital.jee.oauth.spring.client;

import com.forbesdigital.jee.oauth.spring.AbstractBasicAuthenticationFilter;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidClientException;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Specific implementation of Basic Authentication filter for Client Authentication.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class ClientBasicAuthenticationFilter extends AbstractBasicAuthenticationFilter {
	public ClientBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	@Override
	protected void handleMissingBasicAuthenticationHeader() {
		throw new InvalidClientException("No basic authentication header for client authentication is present in the headers.");
	}
}
