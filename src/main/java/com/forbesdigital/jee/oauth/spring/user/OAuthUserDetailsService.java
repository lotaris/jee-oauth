package com.forbesdigital.jee.oauth.spring.user;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * Represent the Spring Security UserDetails service for the users
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class OAuthUserDetailsService implements UserDetailsService {

	private IOAuthUserDetailsBuilder builder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
		OAuthUserDetails details = builder.buildUserDetails(email);

		if (details == null) {
			throw new UsernameNotFoundException("User not found.");
		}
		return details;
	}

	/**
	 * @param builder The new value of user service
	 */
	public void setUserDetailsBuilder(IOAuthUserDetailsBuilder builder) {
		this.builder = builder;
	}
}
