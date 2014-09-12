package com.forbesdigital.jee.oauth;

import com.forbesdigital.jee.oauth.spring.token.exceptions.OAuthAccessDeniedException;
import com.forbesdigital.jee.oauth.spring.token.OAuthTokenDetails;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The OAuth scope filter enforces access to the REST operation based on the two OAuth 
 * annotations (<code>@AllOAuthScopes(...)</code> and <code>@AnyOAuthScopes(...)</code>).
 * 
 * <ul>
 * <li>If no annotation is present, the access to the operation is denied</li>
 * <li>If both annotations are present, the <code>@AllOAuthScopes(...)</code> takes 
 *     priority (the <code>@AnyOAuthScopes(...)</code> is ignored)</li>
 * <li>If the <code>@AllOAuthScopes(...)</code> is present and is empty, the resource 
 *     can be accessed without considering scopes (it still requires a valid OAuth token
 *     though)</li>
 * <li>If the <code>@AllOAuthScopes(...)</code> is present and contains scopes, the 
 *     resource can only be accessed if a valid token containing all the required scopes
 *     is used</li>
 * <li>If the <code>@AnyOAuthScopes(...)</code> is present (on its own) and is empty, the 
 *     resource can be accessed without considering scopes (it still requires a valid 
 *     OAuth token though)</li>
 * <li>If the <code>@AnyOAuthScopes(...)</code> is present (on its own) and contains
 *     scopes, the resource can only be accessed if a valid token containing at least one
 *     of the scopes is used</li>
 * </ul>
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @author Florian Poulin <florian.poulin@forbes-digital.com>
 */
public class OAuthScopeFilter implements ContainerRequestFilter {

	//TODO - add a @NoOAuthScopesRequired annotation
	// TODO - make sure that we do not need to put the @OAuth annotation on the token endpoint
	/**
	 * Cache of OAuth scopes granting access to the REST Method Resource
	 */
	private final Set<String> allScopes;
	private final Set<String> anyScopes;

	/**
	 * Constructor.
	 *
	 * @param allScopes The list of OAuth scopes required to get access to the annotated 
	 * resource. The token used to do the API operation must have at least all the scopes 
	 * declared in this annotation.
	 * @param anyScopes The list of OAuth scopes granting access to the annotated 
	 * resource. The token used to do the API operation must have at least one of the 
	 * scopes declared in this annotation.
	 */
	public OAuthScopeFilter(Set<String> allScopes, Set<String> anyScopes) {
		this.allScopes = allScopes;
		this.anyScopes = anyScopes;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		// if there is no annotation at all, access is denied by default
		if (allScopes == null && anyScopes == null) {
			throw new OAuthAccessDeniedException(); // HTTP 403
		}
		
		// handle required scopes ("allOAuthScopes" annotation)
		if (allScopes != null) {
			
			// give access if no scope is explicitly required
			if (allScopes.isEmpty()) {
				return;
			}
			
			// otherwise, check that the token has all the required scopes
			String role = null;
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				role = ((OAuthTokenDetails) authentication.getPrincipal()).getClientRole();
				Set<String> gaStr = new TreeSet<>();
				for (GrantedAuthority ga : authentication.getAuthorities()) {
					gaStr.add(ga.getAuthority());
				}
				if (gaStr.containsAll(allScopes)) {
					return;
				}
			}
			throw new OAuthAccessDeniedException(allScopes, role); // HTTP 403
		}
		
		// give access if no scope is explicitly listed in the "anyOAuthScopes" annotation
		if (anyScopes.isEmpty()) {
			return;
		}
		
		// otherwise, check that the token has at least one of the allowed scopes
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (anyScopes.contains(ga.getAuthority())) {
					return;
				}
			}
		}
		throw new OAuthAccessDeniedException(); // HTTP 403
	}
}
