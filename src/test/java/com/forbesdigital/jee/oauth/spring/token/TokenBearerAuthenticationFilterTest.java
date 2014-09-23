package com.forbesdigital.jee.oauth.spring.token;

import com.forbesdigital.jee.oauth.spring.token.exceptions.MalformedBearerTokenException;
import com.forbesdigital.jee.oauth.spring.token.exceptions.MissingAuthorizationHeaderException;
import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyObject;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test suite for TokenBearerAuthenticationFilter class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"tokenBearerAuthenticationFilter"})
public class TokenBearerAuthenticationFilterTest {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String VALID_AUTHORIZATION_HEADER = "Bearer 1234567890";
	private static final String SECOND_VALID_AUTHORIZATION_HEADER = "Bearer 1,234567890";
	private static final String INVALID_AUTHORIZATION_HEADER = "INVALID 1234567890";
	private static final String MALFORMED_AUTHORIZATION_HEADER = "Bearer 1%&*fsdf@sd";

	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private HttpServletRequest request;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private Authentication authentication;
	@Mock
	private AnonymousAuthenticationToken anonymousAuthenticationToken;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private FilterChain chain;
	@Mock
	private AuthenticationException authenticationException;

	@InjectMocks
	private TokenBearerAuthenticationFilter filter;
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		SecurityContextHolder.setContext(securityContext);

		
	}

	/**
	 * Test of doFilter method, of class TokenBearerAuthenticationFilter.
	 * @throws java.lang.Exception
	 */
	@Test
	@RoxableTest(key = "46d93329ea24")
	public void testDoFilterWithSuccess() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(VALID_AUTHORIZATION_HEADER);
		try {
			filter.doFilter(request, null, chain);
		} catch (MissingAuthorizationHeaderException ex) {
			fail("The authorization header should have been present.");
		} catch (MalformedBearerTokenException ex) {
			fail("The token should have been valid.");
		} catch (AuthenticationException ex) {
			fail("The authentication should have succeeded.");
		}
	}
	
	/**
	 * Test of doFilter method with invalid authorization header.
	 * @throws java.lang.Exception
	 */
	@Test
	@RoxableTest(key = "e2a111cc3400")
	public void testDoFilterWithInvalidAuthorizationHeader() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(INVALID_AUTHORIZATION_HEADER);
		try {
			filter.doFilter(request, null, chain);
			fail("The token should have not been valid.");
		} catch (MissingAuthorizationHeaderException ex) {
			
		}
	}
	
	/**
	 * Test of doFilter method with malformed authorization header.
	 * @throws java.lang.Exception
	 */
	@Test
	@RoxableTest(key = "2f44f1bbc97e")
	public void testDoFilterWithMalformedAuthorizationHeader() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(MALFORMED_AUTHORIZATION_HEADER);
		try {
			filter.doFilter(request, null, chain);
			fail("The token should have not been valid.");
		} catch (MalformedBearerTokenException ex) {
			
		}
	}
	
	/**
	 * Test of doFilter method with unsuccessful authentication.
	 * @throws java.lang.Exception
	 */
	@Test
	@RoxableTest(key = "f84bb1f612ac")
	public void testDoFilterWithUnsuccessfulAuthentication() throws Exception {
		when(authenticationManager.authenticate((Authentication) anyObject())).thenThrow(authenticationException);
		when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(VALID_AUTHORIZATION_HEADER);
		try {
			filter.doFilter(request, null, chain);
			fail("The authentication should not have been successful.");
		} catch (AuthenticationException ex) {
			
		}
	}
	
	/**
	 * Test of doFilter method where authentication is not required because user is already authenticated.
	 * @throws java.lang.Exception
	 */
	@Test
	@RoxableTest(key = "8f96f3062956")
	public void testDoFilterWhereAuthenticationIsNotRequired() throws Exception {
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(SECOND_VALID_AUTHORIZATION_HEADER);
		try {
			filter.doFilter(request, null, chain);
		} catch (MissingAuthorizationHeaderException ex) {
			fail("The authorization header should have been present.");
		} catch (MalformedBearerTokenException ex) {
			fail("The token should have been valid.");
		} catch (AuthenticationException ex) {
			fail("The authentication should have succeeded.");
		}
	}

}
