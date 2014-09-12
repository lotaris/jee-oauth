package com.forbesdigital.jee.oauth;

import java.util.Arrays;
import java.util.TreeSet;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * The dynamic feature ensure that the REST application apply the right scope filtering.
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 * @see OAuthScopeFilter
 */
@Provider
public class OAuthScopeDynamicFeature implements DynamicFeature {

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		
		// Retrieve the OAuthScope annotations on the REST Resource Method
		AllOAuthScopes requiredScopes = resourceInfo.getResourceMethod().getAnnotation(AllOAuthScopes.class);
		AnyOAuthScopes allowedScopes = resourceInfo.getResourceMethod().getAnnotation(AnyOAuthScopes.class);
		
		// Register a filter with values obtained from OAuth authorization annotations
		context.register(new OAuthScopeFilter(
			requiredScopes == null ? null : new TreeSet<>(Arrays.asList(requiredScopes.value())),
			allowedScopes  == null ? null : new TreeSet<>(Arrays.asList(allowedScopes.value()))
		));
	}
}
