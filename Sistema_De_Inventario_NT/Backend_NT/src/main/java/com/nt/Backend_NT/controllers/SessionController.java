package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.UserEntity;
import com.nt.Backend_NT.model.SessionResponse;
import com.nt.Backend_NT.repositories.UserRepository;
import com.nt.Backend_NT.util.CustomUserDetails;
import com.nt.Backend_NT.util.JwtUtil;

@RestController
@RequestMapping("/session")
public class SessionController {
	
	@Autowired
	AuthenticationManager authManager;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserDetailsService userDatailsService;
	@Autowired
	JwtUtil jwtUtil;
	
	@PostMapping
	public ResponseEntity<SessionResponse> obtenerSesion(@RequestBody UserEntity usuario) throws Exception{
		
		try {
			Authentication authentication;
			authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken
					(usuario.getUsuario(),usuario.getClave()));
		}catch(Exception ex) {
			throw new Exception("Usuario o contraseña incorrectas");
		}
		
		CustomUserDetails userDetails = (CustomUserDetails) 
				userDatailsService.loadUserByUsername(usuario.getUsuario());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return new ResponseEntity<SessionResponse>(new SessionResponse(jwt), HttpStatus.CREATED);
	}


}
