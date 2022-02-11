package com.nt.Backend_NT.util;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	@Value("${secretKey}")
	private String SECRET_KEY;
	@Value("${jwtExpireTime}")
	private long jwtExpireTime;
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
		
	public String extractId(String token){
		return extractClaim(token, Claims::getId);
	}
	
	public boolean isTokenExpired(String token) {
		
		boolean isExpired = extractExpiration(token).before(new Date());
		Logger log = Logger.getLogger(this.getClass());
		log.info(String.format("Token expirado: %s",isExpired ));
		return isExpired;
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY)
				.parseClaimsJws(token).getBody();
	}
	
	public String generateToken(CustomUserDetails userDetails) {
		return createToken(userDetails.getClaims(),userDetails.getUsername());
	}
	
	public String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+jwtExpireTime))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}
	
	public Boolean validateToken(String token, CustomUserDetails userDetails) {
		final String username = extractUsername(token);
		final ObjectMapper om = new ObjectMapper();
		final CustomClaims customClaims = om.convertValue(
				Jwts.parser().setSigningKey(SECRET_KEY)
				.parseClaimsJws(token).getBody()
				, CustomClaims.class);;
		boolean a = username.equals(userDetails.getUsername());
		boolean b = customClaims.get_id().equals(userDetails.getClaims().get("_id"));
		boolean c = !isTokenExpired(token);
		return a&&b&&c;
	}
}
