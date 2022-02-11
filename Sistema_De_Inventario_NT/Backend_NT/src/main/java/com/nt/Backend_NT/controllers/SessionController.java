package com.nt.Backend_NT.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.UserEntity;
import com.nt.Backend_NT.model.SessionResponse;
import com.nt.Backend_NT.repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/sesion")
public class SessionController {
	
	@Autowired
	UserRepository usuarioR;
	@Value("${secretKey}")
	String secretKey;
	@Value("${jwtExpireTime}")
	long jwtExpireTime;
	
	@GetMapping
	public ResponseEntity<SessionResponse> obtenerSesion(@RequestBody UserEntity usuario) {
//		Authentication authentication;
//		authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken
//				(usuario.getUsuario(),usuario.getClave()));
//		
//		if(authentication.isAuthenticated()) {
//			return getToken(authentication, usuario);
//		}
//		return "Error";
		SessionResponse sesion = new SessionResponse();
		sesion.setToken(getToken(null, usuario));
		
		return new ResponseEntity<SessionResponse>(sesion, HttpStatus.CREATED);
	}
	
	private String getToken(Authentication auth, UserEntity user) {
		Map<String, Object> claimMap= new HashMap<String, Object>();
		claimMap.put("id", user.getId());
		claimMap.put("usuario", user.getUsuario());
		
		String token = Jwts.builder()
					.setIssuedAt(new Date())
					.addClaims(claimMap)
					.setExpiration(new Date(System.currentTimeMillis()+jwtExpireTime))
					.signWith(SignatureAlgorithm.HS256, secretKey)
					.compact();
	
		
		return token;
	}

}
