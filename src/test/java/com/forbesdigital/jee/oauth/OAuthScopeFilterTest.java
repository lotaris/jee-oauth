package com.forbesdigital.jee.oauth;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.forbesdigital.jee.oauth.spring.token.OAuthTokenDetails;
import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.container.ContainerRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test suite for OAuthScopeFilter class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"oAuthScopeFilter"})
public class OAuthScopeFilterTest {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	private static final String BASIC_CLIENT_SCOPE = "basic_scope";
	private static final String TRUSTED_CLIENT_SCOPE = "trusted_scope";
	private static final String ADVANCED_CLIENT_SCOPE = "advanced_scope";
	private static final String SUPER_ADVANCED_CLIENT_SCOPE = "super_advanced_scope";
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@Mock
	private SecurityContext securityContext;
	@Mock
	private Authentication authentication;
	@Mock
	private GrantedAuthority basicGrantedAuthoriry;
	@Mock
	private GrantedAuthority trustedGrantedAuthoriry;
	@Mock
	private GrantedAuthority advancedGrantedAuthoriry;
	//</editor-fold>

	private OAuthScopeFilter oAuthScopefilter;



	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		String accessToken = "accessToken";
		OAuthTokenDetails tokenDetails = new OAuthTokenDetails(123L, accessToken, new Date(), "clientKey", "userKey", null, "clientRole", null);
		when(authentication.getPrincipal()).thenReturn(tokenDetails);
	}

	/**
	 * Test of filter method where AllScopes match.
	 */
	@Test
	@RoxableTest(key = "5c11a2dfee8e")
	public void testFilterWhereAllScopesMatch() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(basicGrantedAuthoriry);
		authorities.add(trustedGrantedAuthoriry);
		authorities.add(advancedGrantedAuthoriry);
		
		Set<String> allScopes = new HashSet<>();
		allScopes.add(BASIC_CLIENT_SCOPE);
		allScopes.add(TRUSTED_CLIENT_SCOPE);
		Set<String> anyScopes = null;
		
		oAuthScopefilter = new OAuthScopeFilter(allScopes, anyScopes);

		when(basicGrantedAuthoriry.getAuthority()).thenReturn(BASIC_CLIENT_SCOPE);
		when(trustedGrantedAuthoriry.getAuthority()).thenReturn(TRUSTED_CLIENT_SCOPE);
		when(advancedGrantedAuthoriry.getAuthority()).thenReturn(ADVANCED_CLIENT_SCOPE);
		when(authentication.getAuthorities()).thenReturn((Collection) authorities);

		try {
			oAuthScopefilter.filter(null);
		} catch (IOException ex) {
			fail("No exception should have been thrown.");
		}
	}

	/**
	 * Test of filter method where AnyScopes match.
	 */
	@Test
	@RoxableTest(key = "4281c9223712")
	public void testFilterWhereAnyScopesMatch() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(basicGrantedAuthoriry);
		authorities.add(trustedGrantedAuthoriry);
		authorities.add(advancedGrantedAuthoriry);
		
		Set<String> allScopes = null;
		Set<String> anyScopes = new HashSet<>();
		anyScopes.add(BASIC_CLIENT_SCOPE);
		anyScopes.add(TRUSTED_CLIENT_SCOPE);
		anyScopes.add(ADVANCED_CLIENT_SCOPE);

		oAuthScopefilter = new OAuthScopeFilter(allScopes, anyScopes);

		when(basicGrantedAuthoriry.getAuthority()).thenReturn(BASIC_CLIENT_SCOPE);
		when(trustedGrantedAuthoriry.getAuthority()).thenReturn(TRUSTED_CLIENT_SCOPE);
		when(advancedGrantedAuthoriry.getAuthority()).thenReturn(ADVANCED_CLIENT_SCOPE);
		when(authentication.getAuthorities()).thenReturn((Collection) authorities);

		ContainerRequestContext requestContext = null;
		try {
			oAuthScopefilter.filter(requestContext);
		} catch (IOException ex) {
			fail("No exception should have been thrown.");
		}
	}

	/**
	 * Test of filter method where AnyScopes and AllScopes are null.
	 */
	@Test
	@RoxableTest(key = "097d813636af")
	public void testFilterWhereAnyScopesAndAllScopesAreNull() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		Set<String> allScopes = null;
		Set<String> anyScopes = null;
		oAuthScopefilter = new OAuthScopeFilter(allScopes, anyScopes);

		ContainerRequestContext requestContext = null;
		try {
			oAuthScopefilter.filter(requestContext);
			fail("Exception should have been thrown.");
		} catch (IOException ex) {
		}
	}

	/**
	 * Test of filter method where AnyScopes is null and AllScopes is empty.
	 */
	@Test
	@RoxableTest(key = "5614bb8ed02c")
	public void testFilterWhereAnyScopesIsNullAndAllScopesIsEmpty() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		Set<String> allScopes = new HashSet<>();
		Set<String> anyScopes = null;
		oAuthScopefilter = new OAuthScopeFilter(allScopes, anyScopes);

		ContainerRequestContext requestContext = null;
		try {
			oAuthScopefilter.filter(requestContext);
		} catch (IOException ex) {
			fail("No exception should have been thrown.");
		}
	}

	/**
	 * Test of filter method where AnyScopes is empty and AllScopes is null.
	 */
	@Test
	@RoxableTest(key = "ee20ba3462cf")
	public void testFilterWhereAnyScopesIsEmptyAndAllScopesIsNull() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		Set<String> allScopes = null;
		Set<String> anyScopes = new HashSet<>();
		oAuthScopefilter = new OAuthScopeFilter(allScopes, anyScopes);

		ContainerRequestContext requestContext = null;
		try {
			oAuthScopefilter.filter(requestContext);
		} catch (IOException ex) {
			fail("No exception should have been thrown.");
		}
	}

	/**
	 * Test of filter method where AllScopes do not match.
	 */
	@Test
	@RoxableTest(key = "8000eaa53104")
	public void testFilterWhereAllScopesDoNotMatch() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(basicGrantedAuthoriry);
		authorities.add(trustedGrantedAuthoriry);
		authorities.add(advancedGrantedAuthoriry);
		Set<String> allScopes = new HashSet<>();
		allScopes.add(BASIC_CLIENT_SCOPE);
		allScopes.add(SUPER_ADVANCED_CLIENT_SCOPE);
		Set<String> anyScopes = new HashSet<>();
		anyScopes.add(BASIC_CLIENT_SCOPE);
		anyScopes.add(TRUSTED_CLIENT_SCOPE);
		anyScopes.add(ADVANCED_CLIENT_SCOPE);

		oAuthScopefilter = new OAuthScopeFilter(allScopes, anyScopes);

		when(basicGrantedAuthoriry.getAuthority()).thenReturn(BASIC_CLIENT_SCOPE);
		when(trustedGrantedAuthoriry.getAuthority()).thenReturn(TRUSTED_CLIENT_SCOPE);
		when(advancedGrantedAuthoriry.getAuthority()).thenReturn(ADVANCED_CLIENT_SCOPE);
		when(authentication.getAuthorities()).thenReturn((Collection) authorities);

		ContainerRequestContext requestContext = null;
		try {
			oAuthScopefilter.filter(requestContext);
			fail("Exception should have been thrown.");
		} catch (IOException ex) {

		}
	}

	/**
	 * Test of filter method where AnyScopes do not match.
	 */
	@Test
	@RoxableTest(key = "361b0325900a")
	public void testFilterWhereAnyScopesDoNotMatch() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(basicGrantedAuthoriry);
		authorities.add(trustedGrantedAuthoriry);
		authorities.add(advancedGrantedAuthoriry);
		Set<String> allScopes = null;
		Set<String> anyScopes = new HashSet<>();
		anyScopes.add(ADVANCED_CLIENT_SCOPE);

		oAuthScopefilter = new OAuthScopeFilter(allScopes, anyScopes);

		when(basicGrantedAuthoriry.getAuthority()).thenReturn(BASIC_CLIENT_SCOPE);
		when(trustedGrantedAuthoriry.getAuthority()).thenReturn(TRUSTED_CLIENT_SCOPE);
		when(authentication.getAuthorities()).thenReturn((Collection) authorities);

		ContainerRequestContext requestContext = null;
		try {
			oAuthScopefilter.filter(requestContext);
			fail("Exception should have been thrown.");
		} catch (IOException ex) {

		}
	}

}
