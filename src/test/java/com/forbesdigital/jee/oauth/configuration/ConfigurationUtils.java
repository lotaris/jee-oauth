package com.forbesdigital.jee.oauth.configuration;

/**
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
public class ConfigurationUtils {

	private final IOAuthConfiguration configuration;
	
	private static ConfigurationUtils obj;
	
	private ConfigurationUtils(IOAuthConfiguration configuration){
		
		this.configuration = configuration;
		OAuthContext.registerConfiguration(configuration);
	}
	
	public static ConfigurationUtils getInstance(IOAuthConfiguration configuration) {
		if(obj == null){
			obj = new ConfigurationUtils(configuration);
		}
		
		return obj;
	}

}
