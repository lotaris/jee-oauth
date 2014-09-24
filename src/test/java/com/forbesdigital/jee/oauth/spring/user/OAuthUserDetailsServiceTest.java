package com.forbesdigital.jee.oauth.spring.user;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

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

	@Test
	@RoxableTest(key = "90fb7f206581")
	public void loadUserForAValidEmail() {
		String email = "";
		when(builder.buildUserDetails(email)).thenReturn(details);
		UserDetails result = service.loadUserByUsername(email);
		assertNotNull(result);
	}

	@Test
	@RoxableTest(key = "e7efbc71a899")
	public void loadUserForAInexsistentEmail() {
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
