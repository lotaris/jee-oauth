package com.forbesdigital.jee.oauth.spring.client;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represent the client details for Spring Security
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class OAuthClientDetails implements UserDetails {
	
	private Long id;
	private String key;
	private String clientId;
	private String clientSecret;
	private String clientRole;
	
	private Collection<GrantedAuthority> authorities;

	/**
	 * Constructor
	 * 
	 * @param id The id of the client
	 * @param key The key of the client
	 * @param clientId The OAuth client_id of the client
	 * @param clientSecret The OAuth client_secret of the client
	 * @param clientRole The Client role
	 * @param authorities The permissions
	 */
	public OAuthClientDetails(Long id, String key, String clientId, String clientSecret, String clientRole, Collection<GrantedAuthority> authorities) {
		this.id = id;
		this.key = key;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.authorities = authorities;
		this.clientRole = clientRole;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return clientSecret;
	}

	public Long getId() {
		return id;
	}

	public String getClientKey() {
		return key;
	}
	
	public String getClientRole() {
		return clientRole;
	}

	@Override
	public String getUsername() {
		return clientId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private static final long serialVersionUID = 955659725892219610L;
	
}
