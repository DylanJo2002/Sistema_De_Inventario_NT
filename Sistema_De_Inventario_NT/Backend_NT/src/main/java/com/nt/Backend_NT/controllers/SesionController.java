package com.nt.Backend_NT.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.UsuarioEntity;
import com.nt.Backend_NT.repositories.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/sesion")
public class SesionController {
	
	@Autowired
	UsuarioRepository usuarioR;
	AuthenticationManager authManager;
	@Value("${secretKey}")
	String secretKey;
	@Value("${jwtExpireTime}")
	String jwtExpireTime;
	
	public SesionController(AuthenticationManager authManager) {
		this.authManager = authManager;
	}
	
	@PostMapping
	public String obtenerSesion(UsuarioEntity usuario) {
		Authentication authentication;
		authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken
				(usuario.getUsuario(),usuario.getClave()));
		
		if(authentication.isAuthenticated()) {
			return getToken(authentication, usuario);
		}
		return "Error";
	}
	
	private String getToken(Authentication auth, UsuarioEntity user) {
		Map<String, Object> claimMap= new HashMap<String, Object>();
		claimMap.put("id", user.getId());
		claimMap.put("usuario", user.getUsuario());
		
		String token = Jwts.builder()
					.setIssuedAt(new Date())
					.setSubject(auth.getName())
					.addClaims(claimMap)
					.setExpiration(new Date(System.currentTimeMillis()+jwtExpireTime))
					.signWith(SignatureAlgorithm.HS256, secretKey)
					.compact();
	
		
		return token;
	}

}
