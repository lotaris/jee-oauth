package com.forbesdigital.jee.oauth.spring.user;

import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidGrantException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidRequestException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

/**
 * Test suite for UserPasswordGrantTypeAuthenticationFilter class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"tokenBearerAuthenticationFilter"})
public class UserPasswordGrantTypeAuthenticationFilterTest {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	private final static String GRANT_TYPE = "grant_type";
	private final static String CLIENT_CREDENTIALS = "client_credentials";
	private final static String PASSWORD = "password";
	private final static String USERNAME = "username";
	private final static String USERNAME_PARAMETER = "myusername";
	private final static String PASSWORD_PARAMETER = "mypassword";

	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private HttpServletRequest request;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private Authentication authentication;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private FilterChain chain;

	@InjectMocks
	private UserPasswordGrantTypeAuthenticationFilter filter;
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authenticationManager.authenticate((Authentication) anyObject())).thenReturn(authentication);
	}

	@Test
	@RoxableTest(key = "06f714e426f5")
	public void doFilterWithGrantTypePassword() throws Exception {
		when(authentication.isAuthenticated()).thenReturn(true);
		when(request.getParameter(GRANT_TYPE)).thenReturn(PASSWORD);
		when(request.getParameter(USERNAME)).thenReturn(USERNAME_PARAMETER);
		when(request.getParameter(PASSWORD)).thenReturn(PASSWORD_PARAMETER);
		try {
			filter.doFilter(request, null, chain);
		} catch (AccessDeniedException ex) {
			fail("A valid user should have been authenticated.");
		} catch (InvalidRequestException ex) {
			fail("The request sent should have been valid.");
		} catch (InvalidGrantException ex) {
			fail("The grant_type sent should have been valid.");
		}
	}

	@Test
	@RoxableTest(key = "fad8fd45ae2e")
	public void doFilterWithGrantTypeClientCredentials() throws Exception {
		when(authentication.isAuthenticated()).thenReturn(true);
		when(request.getParameter(GRANT_TYPE)).thenReturn(CLIENT_CREDENTIALS);
		try {
			filter.doFilter(request, null, chain);
		} catch (AccessDeniedException ex) {
			fail("A valid user should have been authenticated.");
		} catch (InvalidRequestException ex) {
			fail("The request sent should have been valid.");
		} catch (InvalidGrantException ex) {
			fail("The grant_type sent should have been valid.");
		}
	}
	
	@Test
	@RoxableTest(key = "9e512360db9c")
	public void doFilterWhenUserIsNotAuthenticated() throws Exception {
		when(authentication.isAuthenticated()).thenReturn(false);
		when(request.getParameter(GRANT_TYPE)).thenReturn(CLIENT_CREDENTIALS);
		try {
			filter.doFilter(request, null, chain);
			fail("A user should not have been authenticated.");
		} catch (AccessDeniedException ex) {
			
		}
	}
	
	@Test
	@RoxableTest(key = "1c05638839a4")
	public void doFilterWhereUserHasNoPasswordSet() throws Exception {
		when(authentication.isAuthenticated()).thenReturn(true);
		when(request.getParameter(GRANT_TYPE)).thenReturn(PASSWORD);
		when(request.getParameter(USERNAME)).thenReturn(USERNAME_PARAMETER);
		when(request.getParameter(PASSWORD)).thenReturn(null);
		try {
			filter.doFilter(request, null, chain);
			fail("The request sent should not have been valid.");
		} catch (InvalidRequestException ex) {
			
		}
	}
}
