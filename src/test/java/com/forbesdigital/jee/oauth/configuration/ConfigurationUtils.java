package com.forbesdigital.jee.oauth.configuration;

/**
 * This class is used to have access on unit tests methods on the package private methods in OAuthContext class.
 * 
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
public class ConfigurationUtils {
	public static void registerConfiguration(IOAuthConfiguration configuration){
		OAuthContext.registerConfiguration(configuration);
	}
	
	public static void unregisterConfiguration(){
		OAuthContext.unregisterConfiguration();
	}
}
