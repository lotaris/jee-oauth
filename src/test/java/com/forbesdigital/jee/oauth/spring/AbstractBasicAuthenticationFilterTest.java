package com.forbesdigital.jee.oauth.spring;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

/**
 * Test suite for AbstractBasicAuthenticationFilter class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"abstractBasicAuthenticationFilter"})
public class AbstractBasicAuthenticationFilterTest {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	private static final String AUTHORIZATION = "Authorization";
	private static final String BASIC_AUTHORIZATION = "Basic TURDMk9QUVFTQUZJQ0RGUVNFNzBHTUxUNkFGTklEOnBIb1VSZHYwaG9pc3F4QWNQOUlEVkVMR0F6QVFacThXcE1HSjlOeUM=";
	private static final String INVALID_AUTHORIZATION = "INVALID 1234124";
	private static final String INVALID_BASIC_AUTHORIZATION = "Basic 123412434234";
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private AuthenticationException authenticationException;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private HttpServletRequest request;
	@Mock
	private Authentication authentication;
	@Mock
	private FilterChain chain;

	@InjectMocks
	private AbstractBasicAuthenticationFilterImpl abstractBasicAuthenticationFilterImpl;
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	@RoxableTest(key = "e109820f263c")
	public void testDoFilterWithSuccess() throws Exception {
		when(request.getHeader(AUTHORIZATION)).thenReturn(BASIC_AUTHORIZATION);
		when(authenticationManager.authenticate((Authentication) anyObject())).thenReturn(authentication);
		try {
			abstractBasicAuthenticationFilterImpl.doFilter(request, null, chain);
		} catch (AccessDeniedException ex) {
			fail("The authorization header should have been valid.");
		} catch (BadCredentialsException ex) {
			fail("The credential provided should have been valid.");
		} catch (AuthenticationException ex) {
			fail("The authentication should have been successful");
		}
	}

	@Test
	@RoxableTest(key = "d6e5224edd27")
	public void doFilterWithInvalidToken() throws Exception {
		when(request.getHeader(AUTHORIZATION)).thenReturn(INVALID_AUTHORIZATION);
		when(authenticationManager.authenticate((Authentication) anyObject())).thenReturn(authentication);
		try {
			abstractBasicAuthenticationFilterImpl.doFilter(request, null, chain);
			fail("The authorization header should not have been valid.");
		} catch (AccessDeniedException ex) {
		}
	}

	@Test
	@RoxableTest(key = "60da16f0826c")
	public void doFilterWithUnsuccessfulAuthentication() throws Exception {
		when(request.getHeader(AUTHORIZATION)).thenReturn(BASIC_AUTHORIZATION);
		when(authenticationManager.authenticate((Authentication) anyObject())).thenThrow(authenticationException);
		try {
			abstractBasicAuthenticationFilterImpl.doFilter(request, null, chain);
			fail("The authentication should not have been successful.");
		} catch (AuthenticationException ex) {
		}
	}

	@Test
	@RoxableTest(key = "6182362334b6")
	public void doFilterWithBadCredentials() throws Exception {
		when(request.getHeader(AUTHORIZATION)).thenReturn(INVALID_BASIC_AUTHORIZATION);
		when(authenticationManager.authenticate((Authentication) anyObject())).thenReturn(authentication);
		try {
			abstractBasicAuthenticationFilterImpl.doFilter(request, null, chain);
			fail("The token should have not been valid.");
		} catch (BadCredentialsException ex) {
		}
	}

	public static class AbstractBasicAuthenticationFilterImpl extends AbstractBasicAuthenticationFilter {

		public AbstractBasicAuthenticationFilterImpl() {
			super(null);
		}

		@Override
		public void handleMissingBasicAuthenticationHeader() {
			throw new AccessDeniedException("Test exception thrown");
		}
	}

}
