package com.forbesdigital.jee.oauth.rest.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.forbesdigital.jee.oauth.OAuthTokenError;
import com.forbesdigital.jee.oauth.OAuthTokenResponse;
import com.forbesdigital.jee.oauth.configuration.ConfigurationUtils;
import com.forbesdigital.jee.oauth.configuration.EOAuthGrantType;
import com.forbesdigital.jee.oauth.configuration.IOAuthClientRole;
import com.forbesdigital.jee.oauth.configuration.IOAuthConfiguration;
import com.forbesdigital.jee.oauth.model.IOAuthClient;
import com.forbesdigital.jee.oauth.model.IOAuthToken;
import com.forbesdigital.jee.oauth.model.IOAuthUser;
import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test suite for AbstractAccessTokenResource class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = "abstractAccessTokenResource")
public class AbstractAccessTokenResourceTest {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	private static final String MOCK_TOKEN_TYPE = "mockTokenType";
	private static final String MOCK_ACCESS_TOKEN = "mockAccessToken";
	private static final String GRANT_TYPE = "client_credentials";
	private static final String PASSWORD_GRANT_TYPE = "password";
	private static final String CLIENT_ROLE = "trusted_client_role";
	private static final String CLIENT_SCOPE = "trusted_client_scope";
	private static final String BASIC_CLIENT_SCOPE = "basic_client_scope";
	private static final String ADVANCED_CLIENT_SCOPE = "advanced_client_scope";
	private static final String USERNAME = "myName";
	private static final String PASSWORD = "myPAssword";
	private static final String MALFORMED_TOKEN = "ADVANCED_ACCESS \\\"READ";
	private static final String EXPIRES_IN = "3600";
	private static final String EXPIRES_IN_BADLY_FORMATED = "3600A";
	private static final String EXPIRES_IN_NEGATIVE = "-3600";

	private static final String INVALID_SCOPE = "invalidScope";

	private static final String DATE = "2015-08-23T08:11:22Z";

	//</editor-fold>
	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private IOAuthClient client;
	@Mock
	private IOAuthUser user;
	@Mock
	private IOAuthToken token;
	@Mock
	private IOAuthClientRole role;
	@Mock
	private IOAuthConfiguration configuration;

	@InjectMocks
	private AbstractAccessTokenImpl abstractAccessTokenResource;
	//</editor-fold>

	@Before
	public void setUp() {

		Set<String> client_scopes = new HashSet<>();
		client_scopes.add("trusted_client_scope");
		client_scopes.add("basic_client_scope");
		
		Set<String> grant_type_scopes = new HashSet<>();
		grant_type_scopes.add("trusted_client_scope");
		
		Set<String> all_client_scopes = new HashSet<>();
		all_client_scopes.add("trusted_client_scope");
		all_client_scopes.add("advanced_client_scope");
		all_client_scopes.add("basic_client_scope");

		MockitoAnnotations.initMocks(this);
		when(client.getClientRole()).thenReturn(CLIENT_ROLE);
		when(configuration.getClientRole(CLIENT_ROLE)).thenReturn(role);

		when(configuration.getAllScopes()).thenReturn(all_client_scopes);
		when(configuration.getClientRole(CLIENT_ROLE).getAllowedScopes()).thenReturn(client_scopes);
		when(configuration.getAllowedScopes(EOAuthGrantType.fromValue(GRANT_TYPE))).thenReturn(grant_type_scopes);
		when(configuration.getAllowedScopes(EOAuthGrantType.fromValue(PASSWORD_GRANT_TYPE))).thenReturn(grant_type_scopes);

	}

	/**
	 * Test of requestToken method, of class AbstractAccessTokenResource.
	 */
	@Test
	@RoxableTest(key = "103e1c08a39a")
	public void testSuccesfulTokenRequest() {
		Set<String> client_scopes = new HashSet<>();
		client_scopes.add("trusted_client_scope");
		client_scopes.add("basic_client_scope");
		Calendar expirationDate = Calendar.getInstance();
		expirationDate.set(2015, 7, 23, 11, 11, 22);
		ConfigurationUtils.registerConfiguration(configuration);
		when(token.getScopes()).thenReturn(client_scopes);
		when(token.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
		when(token.getTokenType()).thenReturn(MOCK_TOKEN_TYPE);
		when(token.getExpirationDate()).thenReturn(expirationDate.getTime());
		when(token.getExpiresIn()).thenReturn(Integer.parseInt(EXPIRES_IN));

		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, CLIENT_SCOPE, USERNAME, PASSWORD, EXPIRES_IN);
		OAuthTokenResponse response = (OAuthTokenResponse) result.getEntity();

		assertEquals(response.getExpiresIn().toString(), EXPIRES_IN);
		assertEquals(response.getScope(), BASIC_CLIENT_SCOPE + " " + CLIENT_SCOPE);
		assertEquals(response.getAccessToken(), MOCK_ACCESS_TOKEN);
		assertEquals(response.getTokenType(), MOCK_TOKEN_TYPE);
		assertEquals(response.getExpirationDate(), DATE);
		assertEquals(response.getAdditionalInformation(), new HashMap<>());
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that returns an error describing that the scope provided is not valid.
	 */
	@Test
	@RoxableTest(key = "2dd70f706c89")
	public void testTokenRequestWithInvaildScope() {
		ConfigurationUtils.registerConfiguration(configuration);
		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, INVALID_SCOPE, USERNAME, PASSWORD, EXPIRES_IN);
		
		OAuthTokenError response = (OAuthTokenError) result.getEntity();

		assertEquals(result.getStatus(), 400);
		assertEquals(response.getError(), "invalid_scope");
		assertEquals(response.getErrorDescription(), "The requested scope is invalid.");
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that returns an error describing that the scope provided is not sufficient.
	 */
	@Test
	@RoxableTest(key = "9b60a586e849")
	public void testTokenRequestWithInsufficientAccessRights() {
		ConfigurationUtils.registerConfiguration(configuration);
		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, ADVANCED_CLIENT_SCOPE, USERNAME, PASSWORD, EXPIRES_IN);
		OAuthTokenError response = (OAuthTokenError) result.getEntity();

		assertEquals(result.getStatus(), 400);
		assertEquals(response.getError(), "invalid_scope");
		assertEquals(response.getErrorDescription(), "The requested scope exceeds the scope granted by the resource owner.");
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that returns an error describing that the provided grant-Type does not support that scope.
	 */
	@Test
	@RoxableTest(key = "3406fbd970d8")
	public void testTokenRequestWithInvalidGrantTypeScopes() {
		ConfigurationUtils.registerConfiguration(configuration);
		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, BASIC_CLIENT_SCOPE, USERNAME, PASSWORD, EXPIRES_IN);
		OAuthTokenError response = (OAuthTokenError) result.getEntity();

		assertEquals(result.getStatus(), 400);
		assertEquals(response.getError(), "invalid_scope");
		assertEquals(response.getErrorDescription(), "The requested scope requires a different grant_type.");
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that returns an error describing that the scope provided is malformed.
	 */
	@Test
	@RoxableTest(key = "15ee8a817ae0")
	public void testTokenRequestWithMalformedScopes() {
		ConfigurationUtils.registerConfiguration(configuration);
		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, MALFORMED_TOKEN, USERNAME, PASSWORD, EXPIRES_IN);
		OAuthTokenError response = (OAuthTokenError) result.getEntity();

		assertEquals(result.getStatus(), 400);
		assertEquals(response.getError(), "invalid_scope");
		assertEquals(response.getErrorDescription(), "The requested scope is malformed.");
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that returns the default token when sending the null scope.
	 */
	@Test
	@RoxableTest(key = "e98bb1ab4248")
	public void testTokenRequestWithNoScope() {
		ConfigurationUtils.registerConfiguration(configuration);
		Calendar expirationDate = Calendar.getInstance();
		expirationDate.set(2015, 7, 23, 11, 11, 22);

		Set<String> client_scopes = new HashSet<>();
		client_scopes.add("trusted_client_scope");
		client_scopes.add("basic_client_scope");

		when(token.getScopes()).thenReturn(client_scopes);
		when(token.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
		when(token.getExpirationDate()).thenReturn(expirationDate.getTime());
		when(token.getExpiresIn()).thenReturn(Integer.parseInt(EXPIRES_IN));
		when(token.getTokenType()).thenReturn(MOCK_TOKEN_TYPE);

		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, null, USERNAME, PASSWORD, EXPIRES_IN);
		OAuthTokenResponse response = (OAuthTokenResponse) result.getEntity();

		assertEquals(response.getAccessToken(), MOCK_ACCESS_TOKEN);
		assertEquals(response.getScope(), BASIC_CLIENT_SCOPE + " " + CLIENT_SCOPE);
		assertEquals(response.getAdditionalInformation(), new HashMap<>());
		assertEquals(response.getTokenType(), MOCK_TOKEN_TYPE);
		assertEquals(response.getExpiresIn().toString(), EXPIRES_IN);
		assertEquals(response.getExpirationDate(), DATE);
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method where the scope provided is formed with blank spaces.
	 */
	@Test
	@RoxableTest(key = "eccf42766784")
	public void testDoubleSpacesScopesRequestToken() {
		ConfigurationUtils.registerConfiguration(configuration);
		Calendar expirationDate = Calendar.getInstance();
		expirationDate.set(2015, 7, 23, 11, 11, 22);

		Set<String> client_scopes = new HashSet<>();
		client_scopes.add("trusted_client_scope");
		client_scopes.add("basic_client_scope");

		when(token.getScopes()).thenReturn(client_scopes);
		when(token.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
		when(token.getExpirationDate()).thenReturn(expirationDate.getTime());
		when(token.getExpiresIn()).thenReturn(Integer.parseInt(EXPIRES_IN));
		when(token.getTokenType()).thenReturn(MOCK_TOKEN_TYPE);

		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, String.format("%s  %s", CLIENT_SCOPE, CLIENT_SCOPE), USERNAME, PASSWORD, EXPIRES_IN);
		OAuthTokenResponse response = (OAuthTokenResponse) result.getEntity();

		assertEquals(result.getStatus(), 200);

		assertEquals(response.getAccessToken(), MOCK_ACCESS_TOKEN);
		assertEquals(response.getScope(), BASIC_CLIENT_SCOPE + " " + CLIENT_SCOPE);
		assertEquals(response.getAdditionalInformation(), new HashMap<>());
		assertEquals(response.getTokenType(), MOCK_TOKEN_TYPE);
		assertEquals(response.getExpiresIn().toString(), EXPIRES_IN);
		assertEquals(response.getExpirationDate(), DATE);
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method where the client token lifetime is null and expires_in parameter sent is null.
	 */
	@Test
	@RoxableTest(key = "ed89053f4420")
	public void testNullClientTokenLifetimeAndNullExpiresInRequestToken() {
		ConfigurationUtils.registerConfiguration(configuration);
		Calendar expirationDate = Calendar.getInstance();
		expirationDate.set(2015, 7, 23, 11, 11, 22);

		Set<String> client_scopes = new HashSet<>();
		client_scopes.add("trusted_client_scope");
		client_scopes.add("basic_client_scope");

		when(token.getScopes()).thenReturn(client_scopes);
		when(token.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
		when(client.getTokenLifetime()).thenReturn(null);
		when(token.getExpirationDate()).thenReturn(expirationDate.getTime());
		when(token.getTokenType()).thenReturn(MOCK_TOKEN_TYPE);

		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, null, USERNAME, PASSWORD, null);
		OAuthTokenResponse response = (OAuthTokenResponse) result.getEntity();

		assertEquals(result.getStatus(), 200);
		assertEquals(response.getAccessToken(), MOCK_ACCESS_TOKEN);
		assertEquals(response.getScope(), BASIC_CLIENT_SCOPE + " " + CLIENT_SCOPE);
		assertEquals(response.getAdditionalInformation(), new HashMap<>());
		assertEquals(response.getTokenType(), MOCK_TOKEN_TYPE);
		assertEquals(response.getExpiresIn().toString(), "0");
		assertEquals(response.getExpirationDate(), DATE);
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that returns an error describing that the expires_in parameter is badly_formated.
	 */
	@Test
	@RoxableTest(key = "0df9a0bdd609")
	public void testTokenRequestWithBadlyFormatedExpiresIn() {
		ConfigurationUtils.registerConfiguration(configuration);
		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, null, USERNAME, PASSWORD, EXPIRES_IN_BADLY_FORMATED);
		OAuthTokenError response = (OAuthTokenError) result.getEntity();

		assertEquals(result.getStatus(), 400);
		assertEquals(response.getError(), "invalid_request");
		assertEquals(response.getErrorDescription(), "The 'expires_in' parameter is not a valid strictly positive integer value.");
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that returns an error describing that the expires_in parameter is negative.
	 */
	@Test
	@RoxableTest(key = "7bc868e26429")
	public void testTokenRequestWithNegativeExpiresIn() {
		ConfigurationUtils.registerConfiguration(configuration);
		Calendar expirationDate = Calendar.getInstance();
		expirationDate.set(2015, 7, 23, 11, 11, 22);

		Response result = abstractAccessTokenResource.requestToken(GRANT_TYPE, null, USERNAME, PASSWORD, EXPIRES_IN_NEGATIVE);
		OAuthTokenError response = (OAuthTokenError) result.getEntity();

		assertEquals(result.getStatus(), 400);
		assertEquals(response.getError(), "invalid_request");
		assertEquals(response.getErrorDescription(), "The 'expires_in' parameter is not a valid strictly positive integer value.");
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Test of requestToken method that uses the password grant type.
	 */
	@Test
	@RoxableTest(key = "6e4bbc183c26")
	public void testTokenRequestWithPasswordGrantType() {
		ConfigurationUtils.registerConfiguration(configuration);
		Set<String> client_scopes = new HashSet<>();
		client_scopes.add("trusted_client_scope");
		client_scopes.add("basic_client_scope");
		Calendar expirationDate = Calendar.getInstance();
		expirationDate.set(2015, 7, 23, 11, 11, 22);

		when(token.getScopes()).thenReturn(client_scopes);
		when(token.getAccessToken()).thenReturn(MOCK_ACCESS_TOKEN);
		when(token.getTokenType()).thenReturn(MOCK_TOKEN_TYPE);
		when(token.getExpirationDate()).thenReturn(expirationDate.getTime());
		when(token.getExpiresIn()).thenReturn(Integer.parseInt(EXPIRES_IN));

		Response result = abstractAccessTokenResource.requestToken(PASSWORD_GRANT_TYPE, CLIENT_SCOPE, USERNAME, PASSWORD, EXPIRES_IN);
		OAuthTokenResponse response = (OAuthTokenResponse) result.getEntity();

		assertEquals(response.getExpiresIn().toString(), EXPIRES_IN);
		assertEquals(response.getScope(), BASIC_CLIENT_SCOPE + " " + CLIENT_SCOPE);
		assertEquals(response.getAccessToken(), MOCK_ACCESS_TOKEN);
		assertEquals(response.getTokenType(), MOCK_TOKEN_TYPE);
		assertEquals(response.getExpirationDate(), DATE);
		assertEquals(response.getAdditionalInformation(), new HashMap<>());
		ConfigurationUtils.unregisterConfiguration();
	}

	/**
	 * Implementation of AbstractAccessTokenResource used to test the functionalities of this abstract class
	 *
	 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
	 */
	public static class AbstractAccessTokenImpl extends AbstractAccessTokenResource<IOAuthClient, IOAuthUser, IOAuthToken> {

		private IOAuthClient client;
		private IOAuthToken token;
		private IOAuthUser user;

		@Override
		protected IOAuthClient getAuthenticatedClient() {
			return client;
		}

		@Override
		protected IOAuthToken createOAuthToken(IOAuthClient client, Integer tokenLifetime, Set<String> grantedScopes, IOAuthUser user) {
			return token;
		}

		@Override
		protected IOAuthUser getOAuthUser(String username) {
			return user;
		}

	}

}
