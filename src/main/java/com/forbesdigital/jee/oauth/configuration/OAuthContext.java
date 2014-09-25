package com.forbesdigital.jee.oauth.configuration;

/**
 * Makes the OAuth configuration available globally.
 * 
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public class OAuthContext {
	
	private static IOAuthConfiguration configuration;
	
	private OAuthContext(){}
	
	/**
	 * Register the OAuth configuration for this application. This method is internally 
	 * invoked by the OAuth library to make the configuration available everywhere in 
	 * the application.
	 * @param configuration The OAuth configuration for this application
	 */
	static void registerConfiguration(IOAuthConfiguration config) {
		if (configuration != null) {
			throw new OAuthConfigurationException("Trying to configure a new OAuth "
					  + "configuration, but there is already one.");
		}
		configuration = config;
	}
	
	/**
	 * Unregister the OAuth configuration for this application. This method is internally invoked by the OAuth library
	 */
	static void unregisterConfiguration(){
		configuration = null;
	}
	
	/**
	 * @return The global OAuth configuration for this application
	 * @see IOAuthConfiguration
	 */
	public static IOAuthConfiguration getConfig() {
		if (configuration == null) {
			throw new OAuthConfigurationException("Could not find the OAuth configuration. "
					  + "Make sure you have registered a Weblistener which extends "
					  + "'com.forbesdigital.library.oauth.configuration.AbstractOAuthConfigurationListener'");
		}
		return configuration;
	}
}
