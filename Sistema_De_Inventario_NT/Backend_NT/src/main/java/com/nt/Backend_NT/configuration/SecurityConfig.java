package com.nt.Backend_NT.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CorsConfigurer<HttpSecurity> cors = new CorsConfigurer<HttpSecurity>();
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/producto").authenticated()
		.and().addFilter(new JWTAuthorizationFilter(authenticationManager()));
	}
	
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
    	List<String> allowedOriginsPatterns = new ArrayList<String>();
    	List<String> allowedMethods = new ArrayList<String>();
    	allowedOriginsPatterns.add("*");
    	allowedMethods.add(RequestMethod.GET.toString());
    	allowedMethods.add(RequestMethod.POST.toString());
    	allowedMethods.add(RequestMethod.PUT.toString());
    	allowedMethods.add(RequestMethod.DELETE.toString());
    	
    	CorsConfiguration cors = new CorsConfiguration();
    	cors.setAllowedOriginPatterns(allowedOriginsPatterns).setAllowedMethods(allowedMethods);
    	
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

}
