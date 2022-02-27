package com.nt.Backend_NT.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.nt.Backend_NT.model.ErrorResponse;
import com.nt.Backend_NT.util.CustomUserDetails;
import com.nt.Backend_NT.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtutil;
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwt = null;
		
		if(authorizationHeader == null) {
			doNoTokenResponse(response);
			return;
		}
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtutil.extractUsername(jwt);
			}catch(Exception e){
				doNoTokenResponse(response);
				return;
			}
			
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
			
			if(jwtutil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
				usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails,null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
				.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String uri = request.getRequestURI();
		return uri.equals("/user")  || uri.equals("/session") || uri.equals("/swagger-ui.html")
				|| uri.contains("swagger") || uri.contains("api-docs");
	}
	
	private void doNoTokenResponse(HttpServletResponse response) throws IOException {
		ErrorResponse error = new ErrorResponse("Token no válido");
		String errorJson = new Gson().toJson(error);
		PrintWriter out = response.getWriter(); 
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		response.setStatus(403);
		out.print(errorJson);
		out.flush();		
	}
}
