package com.forbesdigital.jee.oauth.configuration;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.fail;

/**
 * Test suite for OAuthContext class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"oAuthContext"})
public class OAuthContextTest {

	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private IOAuthConfiguration configuration;
	@Mock
	private IOAuthConfiguration secondConfiguration;
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@After
	public void tearDown() {
		OAuthContext.unregisterConfiguration();
	}

	/**
	 * Test of registerConfiguration method, when a configuration is registered successfully.
	 */
	@Test
	@RoxableTest(key = "fb372db65591")
	public void registerConfigurationWithSuccess() {
		try {
			OAuthContext.registerConfiguration(configuration);
		} catch (OAuthConfigurationException exception) {
			fail("No exception should have been thrown.");
		}
	}

	/**
	 * Test of registerConfiguration method, when a configuration is already registered.
	 */
	@Test
	@RoxableTest(key = "ee5fda548d7d")
	public void registerConfigurationWhenConfigurationAlreadyRegistered() {
		try {
			OAuthContext.registerConfiguration(configuration);
			OAuthContext.registerConfiguration(secondConfiguration);
			fail("Exception should have been thrown.");
		} catch (OAuthConfigurationException exception) {
		}
	}

	/**
	 * Test of getConfig method, where configuration is retrieved with success.
	 */
	@Test
	@RoxableTest(key = "edc459ab5b09")
	public void getConfigurationWithSuccess() {
		try {
			OAuthContext.registerConfiguration(configuration);
			configuration = OAuthContext.getConfig();
		} catch (OAuthConfigurationException exception) {
			fail("No exception should have been thrown.");
		}
	}

	/**
	 * Test of getConfig method, where the call fails because the configuration is null.
	 */
	@Test
	@RoxableTest(key = "4192224f6c0f")
	public void getConfigurationWhenConfigurationIsNull() {
		try {
			configuration = OAuthContext.getConfig();
			fail("Exception should have been thrown.");
		} catch (OAuthConfigurationException exception) {
			
		}
	}
}
