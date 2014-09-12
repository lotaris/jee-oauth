package com.forbesdigital.jee.oauth.spring.token;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represent the token details for Spring Security
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class OAuthTokenDetails implements UserDetails {
	
	private static final long serialVersionUID = 955659725892219610L;
	
	private final Long id;
	private final String accessToken;
	private final Date expirationDate;
	private final String clientKey;
	private final String userKey;
	private final String clientRole;
	private final Collection<GrantedAuthority> authorities;
	private final Map<String, String> properties;
	private Boolean enabled;
		
	/**
	 * Constructor
	 * 
	 * @param id The id of the token
	 * @param accessToken The accessToken
	 * @param expirationDate The expiration date of the token
	 * @param clientKey The key of the client for which the token was generated
	 * @param userKey The key of the user for which the token was generated (if any)
	 * @param authorities The granted authorities
	 * @param clientRole The client role
	 * @param properties Additional properties to keep in the token details
	 */
	public OAuthTokenDetails(Long id, String accessToken, Date expirationDate, String clientKey, 
			  String userKey, Collection<GrantedAuthority> authorities, 
			  String clientRole, Map<String, String> properties) {
		
		this.id = id;
		this.accessToken = accessToken;
		this.expirationDate = expirationDate;
		this.clientKey = clientKey;
		this.userKey = userKey;
		this.authorities = authorities;
		this.clientRole = clientRole;
		this.properties = properties;
		this.enabled = true;
	}

	public void revoke(){
		this.enabled = false;
	}
	
	public Long getId() {
		return id;
	}

	public String getClientKey() {
		return clientKey;
	}
	
	public String getUserKey() {
		return userKey;
	}
	
	public String getClientRole() {
		return clientRole;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	//<editor-fold defaultstate="collapsed" desc="Spring overrides">
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return accessToken;
	}
	
	@Override
	public String getPassword() {
		return "";
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return expirationDate.after(new Date());
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
		return enabled;
	}
	//</editor-fold>
}