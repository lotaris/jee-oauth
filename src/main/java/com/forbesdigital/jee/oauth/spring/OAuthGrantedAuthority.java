package com.forbesdigital.jee.oauth.spring;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represent an authority in terms of Spring Security.
 * 
 * In other words, this authority is a role and/or a permission that
 * can grant a user to have access to something protected with Spring Security.
 * 
 * @author Laurent Prevost, laurent.prevost@lotaris.com
 */
public class OAuthGrantedAuthority implements GrantedAuthority {

	private String name;

	public OAuthGrantedAuthority(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}