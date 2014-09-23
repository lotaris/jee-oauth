package com.forbesdigital.jee.oauth.spring.client;

import com.forbesdigital.jee.oauth.configuration.ConfigurationUtils;
import com.forbesdigital.jee.oauth.configuration.EOAuthGrantType;
import com.forbesdigital.jee.oauth.configuration.IOAuthClientRole;
import com.forbesdigital.jee.oauth.configuration.IOAuthConfiguration;
import com.forbesdigital.jee.oauth.spring.client.exceptions.InvalidRequestException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.UnauthorizedClientException;
import com.forbesdigital.jee.oauth.spring.client.exceptions.UnsupportedGrantTypeException;
import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test suite for GrantTypeCheckFilter class.
 * 
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"grantTypeCheckFilter"})
public class GrantTypeCheckFilterTest {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	
	private static final String CLIENT_CREDENTIALS = "client_credentials";
	private static final String PASSWORD_GRANT_TYPE = "password";
	private static final String UNSUPPORTED_GRANT_TYPE = "invalid_grant_type";
	private static final String BASIC_CLIENT_ROLE = "basic";
	
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private SecurityContext securityContext;
	@Mock
	private IOAuthConfiguration configuration;
	@Mock
	private HttpServletRequest request;
	@Mock
	private IOAuthClientRole clientRole;
	@Mock
	private Authentication authentication;
	@Mock
	private OAuthClientDetails clientDetails;
	@Mock
	private FilterChain filterChain;
	@InjectMocks
	private GrantTypeCheckFilter filter;
	//</editor-fold>
	
	@Before
	public void setUp(){
		Set<EOAuthGrantType> grantTypes = new HashSet<>();
		grantTypes.add(EOAuthGrantType.CLIENT_CREDENTIALS);
		grantTypes.add(EOAuthGrantType.RESOURCE_OWNER_PASSWORD_CREDENTIALS);
		
		
		
		MockitoAnnotations.initMocks(this);
		
		
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.isAuthenticated()).thenReturn(true);
		
		when(configuration.getAllGrantTypes()).thenReturn(grantTypes);
		when(authentication.getPrincipal()).thenReturn(clientDetails);
		when(clientDetails.getClientRole()).thenReturn(BASIC_CLIENT_ROLE);
		when(configuration.getClientRole(BASIC_CLIENT_ROLE)).thenReturn(clientRole);
	}
	
	/**
	 * Test of doFilter method, of class GrantTypeCheckFilter.
	 */
	@Test
	@RoxableTest(key = "dd25a1c92024")
	public void testFilterWithSuccess() throws Exception {
		Set<EOAuthGrantType> allowedGrantTypes = new HashSet<>();
		allowedGrantTypes.add(EOAuthGrantType.CLIENT_CREDENTIALS);
		
		when(request.getParameter("grant_type")).thenReturn(CLIENT_CREDENTIALS);
		when(authentication.isAuthenticated()).thenReturn(true);
		when(clientRole.getAllowedOAuthGrantTypes()).thenReturn(allowedGrantTypes);
		try{
			ConfigurationUtils.registerConfiguration(configuration);
			filter.doFilter(request, null, filterChain);
			ConfigurationUtils.unregisterConfiguration();
		}catch(AccessDeniedException ex){
			fail("A client should have been authenticated.");
		} catch(InvalidRequestException ex){
			fail("Grant type should have not been null.");
		} catch(UnsupportedGrantTypeException ex){
			fail("Grant type should have not been authorized.");
		} catch (UnauthorizedClientException ex){
			fail("The client should have been authorized.");
		} 
	}
	
	/**
	 * Test of doFilter method with null grant type.
	 */
	@Test
	@RoxableTest(key = "feb5054b4d85")
	public void testFilterWithNullGrantType() throws Exception {
		when(request.getParameter("grant_type")).thenReturn(null);
		when(authentication.isAuthenticated()).thenReturn(true);
		try{
			ConfigurationUtils.registerConfiguration(configuration);
			filter.doFilter(request, null, filterChain);
			
			fail("Grant type should have not been null.");
		} catch(InvalidRequestException ex){
			ConfigurationUtils.unregisterConfiguration();
		}
	}
	
	/**
	 * Test of doFilter method with no authenticated client.
	 */
	@Test
	@RoxableTest(key = "fe7a4b49b5f0")
	public void testFilterWithNoAuthenticatedClient() throws Exception {
		when(request.getParameter("grant_type")).thenReturn(null);
		when(authentication.isAuthenticated()).thenReturn(false);
		try{
			ConfigurationUtils.registerConfiguration(configuration);
			filter.doFilter(request, null, filterChain);
			
			fail("Client should not have been authenticated.");
		} catch(AccessDeniedException ex){
			ConfigurationUtils.unregisterConfiguration();
		}
	}
	
	/**
	 * Test of doFilter method with unsupported grant type.
	 */
	@Test
	@RoxableTest(key = "4cf77b9b7102")
	public void testFilterWithUnsupportedGrantType() throws Exception {
		when(request.getParameter("grant_type")).thenReturn(UNSUPPORTED_GRANT_TYPE);
		when(authentication.isAuthenticated()).thenReturn(true);
		try{
			ConfigurationUtils.registerConfiguration(configuration);
			filter.doFilter(request, null, filterChain);
			
			fail("Grant type should not have been valid.");
		} catch(UnsupportedGrantTypeException ex){
			ConfigurationUtils.unregisterConfiguration();
		}
	}
	
	/**
	 * Test of doFilter method with unauthorized client.
	 */
	@Test
	@RoxableTest(key = "")
	public void testFilterWithUnauthorizedClient() throws Exception {
		when(request.getParameter("grant_type")).thenReturn(CLIENT_CREDENTIALS);
		when(authentication.isAuthenticated()).thenReturn(true);
		when(clientRole.getAllowedOAuthGrantTypes()).thenReturn(new HashSet<EOAuthGrantType>());
		try{
			ConfigurationUtils.registerConfiguration(configuration);
			filter.doFilter(request, null, filterChain);
			
			fail("Grant type should not have been valid.");
		} catch(UnauthorizedClientException ex){
			ConfigurationUtils.unregisterConfiguration();
		}
	}
	
}
