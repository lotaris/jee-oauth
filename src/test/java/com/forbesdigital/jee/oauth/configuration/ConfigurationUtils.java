/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.forbesdigital.jee.oauth.configuration;

/**
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
