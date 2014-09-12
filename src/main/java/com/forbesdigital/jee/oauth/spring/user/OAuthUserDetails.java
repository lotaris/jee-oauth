package com.forbesdigital.jee.oauth.spring.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represent the user details for Spring Security
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class OAuthUserDetails implements UserDetails {
	private Long id;
	private String key;
	private String email;
	private String password;
	private String salt;
	private boolean active;
	
	private Collection<GrantedAuthority> authorities;

	/**
	 * Constructor
	 * 
	 * @param id The id of the user
	 * @param key The key of the user
	 * @param email The email of the user
	 * @param password password hash of the user
	 * @param salt The salt for the password
	 * @param active The activation state of the user
	 * @param authorities The permissions
	 */
	public OAuthUserDetails(Long id, String key, String email, String password, String salt, boolean active, Collection<GrantedAuthority> authorities) {
		this.id = id;
		this.key = key;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.active = active;
		this.authorities = authorities;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public Long getId() {
		return id;
	}

	public String getUserKey() {
		return key;
	}
	
	@Override
	public String getUsername() {
		return email;
	}

	public String getSalt() {
		return salt;
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
		return active;
	}

	private static final long serialVersionUID = 955659725892219610L;
}
