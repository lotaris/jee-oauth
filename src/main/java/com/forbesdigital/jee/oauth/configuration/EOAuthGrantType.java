package com.forbesdigital.jee.oauth.configuration;

/**
 * An enumeration for all the OAuth grant types supported by the OAuth library.
 * 
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public enum EOAuthGrantType {

	/**
	 * Client credentials grant type.
	 * @see http://tools.ietf.org/html/rfc6749#section-4.4
	 *//**
	 * Client credentials grant type.
	 * @see http://tools.ietf.org/html/rfc6749#section-4.4
	 */
	CLIENT_CREDENTIALS("client_credentials"),
	
	/**
	 * Resource owner password credentials grant type
	 * @see http://tools.ietf.org/html/rfc6749#section-4.3
	 */
	RESOURCE_OWNER_PASSWORD_CREDENTIALS("password");
	
	// --------------------------------------------------
	//	TO be added later in the OAuth library:
	//	
	// Authorization code grand type
	// @see http://tools.ietf.org/html/rfc6749#section-4.1
	//	AUTHORIZATION_CODE("code"),
	//	
	//	Implicit grand type
	//	@see http://tools.ietf.org/html/rfc6749#section-4.2
	//	IMPLICIT("token");
	// --------------------------------------------------
	
	String specName;
	
	EOAuthGrantType (String specName) {
		this.specName = specName;
	}

	@Override
	public String toString() {
		return specName;
	}
	
	/**
	 * Attempts to convert a grant type String to an enum value. The string parameter is 
	 * expected to follow the grant types naming of rfc6749.
	 *
	 * @param value value to match
	 * @return corresponding value if found, null otherwise
	 */
	public static EOAuthGrantType fromValue(String value) {
		switch(value) {
			case "client_credentials":
				return CLIENT_CREDENTIALS;
			case "password":
				return RESOURCE_OWNER_PASSWORD_CREDENTIALS;
			default:
				return null;
		}
	}
}
