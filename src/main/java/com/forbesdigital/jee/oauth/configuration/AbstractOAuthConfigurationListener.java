package com.forbesdigital.jee.oauth.configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This listener is used as a bootstrap mechanism to configure the OAuth library.
 * 
 * You have to provide an implementation for this abstract class in your application and
 * register it as a Web listener. To do this, create a class in your WAR project and 
 * annotate it with {@link WebListener} as follows:
 * 
 * <p><pre>{@code
 * @WebListener
 * public class MyOAuthListener extends AbstractOAuthConfigurationListener {
 * 
 * 	@Override
 * 	public IOAuthConfiguration getOAuthConfiguration() {
 * 		return new MyOAuthConfiguration();
 * 	}
 * }
 * }</pre>
 * 
 * <p>The registered OAuth configuration will be made available to the OAuth library
 * classes (and to yours) through {@link OAuthContext#getConfig()}.
 * 
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 * @see OAuthContext
 */
public abstract class AbstractOAuthConfigurationListener implements ServletContextListener {

	/**
	 * @return An instance of {@link IOAuthConfiguration}
	 */
	public abstract IOAuthConfiguration getOAuthConfiguration();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		OAuthContext.registerConfiguration(getOAuthConfiguration());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}
