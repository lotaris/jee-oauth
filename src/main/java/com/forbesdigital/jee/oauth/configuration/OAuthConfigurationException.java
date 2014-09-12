package com.forbesdigital.jee.oauth.configuration;

/**
 * Runtime exception thrown if something goes wrong during the OAuth library configuration
 * process.
 * 
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public class OAuthConfigurationException extends RuntimeException {
	
	public OAuthConfigurationException (String message) {
		super(message);
	}
}
