package com.forbesdigital.jee.oauth.spring.token;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for OAuthTokenDetailsService class.
 *
 * @author Andrei Bucin <andrei.bucin@fortech.ro>
 */
@RoxableTestClass(tags = {"oAuthToken"})
public class OAuthTokenDetailsServiceTest {

	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private IOAuthTokenDetailsBuilder builder;
	@InjectMocks
	private OAuthTokenDetailsService oAuthTokenDetailsService;
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@RoxableTest(key = "35c431f80c48")
	public void shouldLoadTokenForAValidAccessToken() {
		String accessToken = "accessToken";
		OAuthTokenDetails tokenDetails = new OAuthTokenDetails(123L, accessToken, new Date(), "clientKey", "userKey", null, "clientRole", null);

		when(builder.buildTokenDetails(anyString())).thenReturn(tokenDetails);

		UserDetails details = oAuthTokenDetailsService.loadUserByUsername(accessToken);
		assertNotNull(details);
	}

	@Test
	@RoxableTest(key = "503ffde031d2")
	public void shouldNotLoadTokenForAnInvalidAccessToken() {
		String accessToken = "accessToken";
		when(builder.buildTokenDetails(anyString())).thenReturn(null);

		try {
			oAuthTokenDetailsService.loadUserByUsername(accessToken);
			fail("Exception should have been thrown.");
		} catch (UsernameNotFoundException ex) {
		}
	}
}
