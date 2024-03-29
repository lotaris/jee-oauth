package com.forbesdigital.jee.oauth.spring.client;

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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @see OAuthClientDetailsService
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = "oAuthClientDetailsService")
public class OAuthClientDetailsServiceTest {

	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private IOAuthClientDetailsBuilder clientDetailsBuilder;

	@InjectMocks
	private OAuthClientDetailsService clientDetailsService;
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@RoxableTest(key = "2fef6c3fe0d1")
	public void shouldLoadClientForAValidClientId() {
		String clientId = "clienId";
		OAuthClientDetails userDetails = new OAuthClientDetails(123L, "key", clientId, "secret", "clientRole", null);

		when(clientDetailsBuilder.buildClientDetails(clientId)).thenReturn(userDetails);

		UserDetails result = clientDetailsService.loadUserByUsername(clientId);
		assertEquals(result, userDetails);
	}

	@Test
	@RoxableTest(key = "111ba0eca727")
	public void shouldNotLoadClientForAnInvalidClientId() {
		String clientId = "clienId";

		when(clientDetailsBuilder.buildClientDetails(clientId)).thenReturn(null);

		try {
			clientDetailsService.loadUserByUsername(clientId);
			fail("Exception should have been thrown.");
		} catch (UsernameNotFoundException ex) {
		}
	}

}
