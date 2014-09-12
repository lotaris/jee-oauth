package com.forbesdigital.jee.oauth.spring.client;

import com.forbesdigital.jee.oauth.configuration.EOAuthGrantType;
import com.forbesdigital.jee.oauth.configuration.OAuthContext;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidRequestException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.UnauthorizedClientException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.UnsupportedGrantTypeException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filter to validate that the grant_type body parameter is present and has an expected value.
 * 
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class GrantTypeCheckFilter extends GenericFilterBean {
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

		if (existingAuth == null || !existingAuth.isAuthenticated()) {
			throw new AccessDeniedException("A client should be already authenticated at this stage.");
		}
		validateGrantTypeParameter(request, existingAuth);		
		chain.doFilter(request, response);
	}

	/**
	 * Validates the grant_type parameter. Check is the parameter is sent, 
	 * if it has one of the expected values and 
	 * if the authenticated client is allowed to request it. 
	 * 
	 * @param request The request
	 * @param existingAuth The existing authentication
	 */
	private void validateGrantTypeParameter(final HttpServletRequest request, Authentication existingAuth) {
		String grantTypeStr = request.getParameter("grant_type");

		if (grantTypeStr == null) {
			throw new InvalidRequestException("The request is missing a required parameter.");
		}
		// check that the grant type exists and can be used in this application
		EOAuthGrantType grantType = EOAuthGrantType.fromValue(grantTypeStr);
		if (grantType == null || !OAuthContext.getConfig().getAllGrantTypes().contains(grantType)) {
			throw new UnsupportedGrantTypeException("The authorization grant type is not supported by the authorization server.");
		}

		String clientRole = ((OAuthClientDetails) existingAuth.getPrincipal()).getClientRole();
		if (!OAuthContext.getConfig().getClientRole(clientRole).getAllowedOAuthGrantTypes().contains(grantType)) {
			throw new UnauthorizedClientException("The authenticated client is not authorized to use this authorization grant type.");
		}
	}
}
