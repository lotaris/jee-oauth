package com.forbesdigital.jee.oauth.spring.user;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Test suite for OAuthUserDetailsService class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"oAuthUserDetailsService"})
public class OAuthUserDetailsServiceTest {

	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private IOAuthUserDetailsBuilder builder;
	@Mock
	private OAuthUserDetails details;
	@InjectMocks
	private OAuthUserDetailsService service;
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test of loadUserByUsername method, of class OAuthUserDetailsService.
	 */
	@Test
	@RoxableTest(key = "90fb7f206581")
	public void testLoadUserForAValidEmail() {
		System.out.println("loadUserByUsername");
		String email = "";
		when(builder.buildUserDetails(email)).thenReturn(details);
		UserDetails result = service.loadUserByUsername(email);
		assertNotNull(result);
	}

	/**
	 * Test of loadUserByUsername method, of class OAuthUserDetailsService.
	 */
	@Test
	@RoxableTest(key = "e7efbc71a899")
	public void testLoadUserForAInexsistentEmail() {
		System.out.println("loadUserByUsername");
		String email = "";
		when(builder.buildUserDetails(email)).thenReturn(null);
		try {
			UserDetails result = service.loadUserByUsername(email);
			fail("Exception should have been thrown");
		} catch (UsernameNotFoundException ex) {
			assertEquals(ex.getMessage(), "User not found.");
		}
	}
}
