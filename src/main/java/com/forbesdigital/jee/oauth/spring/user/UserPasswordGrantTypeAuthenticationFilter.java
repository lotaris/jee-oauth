package com.forbesdigital.jee.oauth.spring.user;

import com.forbesdigital.jee.oauth.configuration.EOAuthGrantType;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidGrantException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidRequestException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Specific implementation of a filter for User credentials validation in case of a token request with grant type password.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class UserPasswordGrantTypeAuthenticationFilter  extends GenericFilterBean {

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private AuthenticationManager authenticationManager;
	private String credentialsCharset = "UTF-8";

	/**
	 * Constructor
	 * 
	 * @param authenticationManager 
	 */
	public UserPasswordGrantTypeAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		
		final boolean debug = logger.isDebugEnabled();
		
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

		if (existingAuth == null || !existingAuth.isAuthenticated()) {
			throw new AccessDeniedException("A client should be already authenticated at this stage.");
		}
		
		String[] tokens = extractGrantTypeAndCredentials(request);
		if (tokens != null) {
			try {
				assert tokens.length == 2;

				String username = tokens[0];

				if (debug) {
					logger.debug("Basic Authentication Authorization header found for user '" + username + "'");
				}

				UsernamePasswordAuthenticationToken authRequest =
						new UsernamePasswordAuthenticationToken(username, tokens[1]);
				authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
				Authentication authResult = authenticationManager.authenticate(authRequest);

				if (debug) {
					logger.debug("Authentication success: " + authResult);
				}
				
				// FIXME OAUTH - add a userKey to ClientDetails in case of succesfull user authentication
				// the userKey will be used in AccessTokenResource to get the user an to link it to the token
				
			} catch (AuthenticationException failed) {
				SecurityContextHolder.clearContext();

				if (debug) {
					logger.debug("Authentication request for failed: " + failed);
				}

				throw new InvalidGrantException("The provided authorization grant is not valid.");
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * Extract the grant type and the User credentials if grant type is password.
	 *
	 * @throws InvalidRequestException if one of the required parameters is missing.
	 */
	private String[] extractGrantTypeAndCredentials(HttpServletRequest request) {
		String grantTypeStr = request.getParameter("grant_type");
		EOAuthGrantType grantType = EOAuthGrantType.fromValue(grantTypeStr);

		if (EOAuthGrantType.RESOURCE_OWNER_PASSWORD_CREDENTIALS.equals(grantType)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if (username == null || password == null) {
				throw new InvalidRequestException("The request is missing a required parameter.");
			}
			return new String[]{username, password};
		} else {
			return null;
		}
	}

	public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void setCredentialsCharset(String credentialsCharset) {
		Assert.hasText(credentialsCharset, "credentialsCharset cannot be null or empty");
		this.credentialsCharset = credentialsCharset;
	}

	protected String getCredentialsCharset(HttpServletRequest httpRequest) {
		return credentialsCharset;
	}
}
