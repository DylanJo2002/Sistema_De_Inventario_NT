package com.nt.Backend_NT.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nt.Backend_NT.entities.UserEntity;

public class CustomUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private UserEntity user;
		
	public CustomUserDetails(UserEntity user) {
		this.user = user;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public String getUsername() {
		return user.getUsuario();
	}
	
	@Override
	public String getPassword() {
		return user.getClave();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	
	public Map<String, Object> getClaims(){
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("_id", String.valueOf(user.getId()));
		return claims;
	}
}
