package com.nt.Backend_NT.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.UserEntity;
import com.nt.Backend_NT.repositories.UserRepository;
import com.nt.Backend_NT.util.CustomUserDetails;

@Service
public class NTUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findUsuarioEntityByUsuario(username);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("El usuario %s no existe", username));
		}
		
		return new CustomUserDetails(user);
	}

}
