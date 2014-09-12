package com.forbesdigital.jee.oauth.spring.token;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Represent the Spring Security TokenDetails service for the tokens
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public class OAuthTokenDetailsService implements UserDetailsService {

	private IOAuthTokenDetailsBuilder builder;

	@Override
	public UserDetails loadUserByUsername(String accessToken) throws UsernameNotFoundException, DataAccessException {
		
		OAuthTokenDetails details = builder.buildTokenDetails(accessToken);
		if (details == null) {
			throw new UsernameNotFoundException("Token not found.");
		}
		return details;
	}

	/**
	 * @param builder Service to build a {@link TokenDetails} instance from an access token
	 */
	public void setTokenDetailsBuilder(IOAuthTokenDetailsBuilder builder) {
		this.builder = builder;
	}
}