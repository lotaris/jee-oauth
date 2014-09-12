package com.forbesdigital.jee.oauth.model;

import java.util.Date;
import java.util.Set;

/**
 * Interface for an OAuth Token. Defines the methods of the OAuth Token which are required by the OAuth lib. 
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public interface IOAuthToken {

	/**
	 * Scope pattern as defined in OAuth RFC:
	 * 
	 * scope       = scope-token *( SP scope-token )
	 * scope-token = 1*( %x21 / %x23-5B / %x5D-7E )
	 */
	static final String SCOPE_PATTERN = "[ !#-\\[\\]-~]*";
	static final String SCOPES_SEPARATOR = " ";
	
	static final int TOKEN_LENGTH = 22;
	static final String TOKEN_PATTERN = "[a-zA-Z0-9-._~+/]*";
	
	/**
	 * @return The access token value
	 */
	String getAccessToken();

	/**
	 * @return The token type
	 */
	String getTokenType();

	/**
	 * @return The token validity time in seconds
	 */
	int getExpiresIn();

	/**
	 * @return The token expiration date
	 */
	Date getExpirationDate();

	/**
	 * @return The granted list of OAuth scopes for the token
	 */
	Set<String> getScopes();	
}
