package com.forbesdigital.jee.oauth.spring.token;

import com.forbesdigital.jee.oauth.model.IOAuthToken;
import com.forbesdigital.jee.oauth.spring.token.exceptions.MalformedBearerTokenException;
import com.forbesdigital.jee.oauth.spring.token.exceptions.MissingAuthorizationHeaderException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
 * Specific implementation of Bearer Authentication filter for Token Authentication.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class TokenBearerAuthenticationFilter extends GenericFilterBean {
	
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private final AuthenticationManager authenticationManager;
	private String credentialsCharset = "UTF-8";

	/**
	 * Constructor
	 * 
	 * @param authenticationManager 
	 */
	public TokenBearerAuthenticationFilter(AuthenticationManager authenticationManager) {
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

		// Check authorization header
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			handleMissingBearerAuthenticationHeader();
		}

		// Check the bearer token itself (decode and authenticate)
		try {
			
			String accessToken = extractAndDecodeHeader(authorizationHeader);
			assert accessToken.length() > 0;
			if (!accessToken.matches(IOAuthToken.TOKEN_PATTERN)) {
				throw new MalformedBearerTokenException("The token received is malformed.");
			}

			if (debug) {
				logger.debug("Bearer Authentication Authorization header found for token '" + accessToken + "'");
			}

			if (authenticationIsRequired(accessToken)) {
				UsernamePasswordAuthenticationToken authRequest =
					new UsernamePasswordAuthenticationToken(accessToken, "");
				authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
				Authentication authResult = authenticationManager.authenticate(authRequest);

				if (debug) {
					logger.debug("Authentication success: " + authResult);
				}

				SecurityContextHolder.getContext().setAuthentication(authResult);
			}

		} catch (AuthenticationException failed) {
			
			SecurityContextHolder.clearContext();
			if (debug) {
				logger.debug("Authentication request for failed: " + failed);
			}
			throw failed;
		}

		chain.doFilter(request, response);
	}

	/**
	 * Handle the case when Authorization header is missing or does not have the expected format.
	 */
	private void handleMissingBearerAuthenticationHeader(){
		throw new MissingAuthorizationHeaderException(
				"No bearer authentication header for token authentication is present in the headers.");
	}
	
	/**
	 * Decodes the header into a token.
	 */
	private String extractAndDecodeHeader(String header) {
		String authHeaderValue = header.substring(7).trim();
		int commaIndex = authHeaderValue.indexOf(',');
		if (commaIndex > 0) {
			authHeaderValue = authHeaderValue.substring(0, commaIndex);
		}
		return authHeaderValue;
	}

	private boolean authenticationIsRequired(String username) {
		
		// Only reauthenticate if username doesn't match SecurityContextHolder and user isn't authenticated (see SEC-53)
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
		if (existingAuth == null || !existingAuth.isAuthenticated()) {
			return true;
		}

		// Limit username comparison to providers which use usernames (ie UsernamePasswordAuthenticationToken) (see SEC-348)
		if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username)) {
			return true;
		}
		return existingAuth instanceof AnonymousAuthenticationToken;
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
