package com.nt.Backend_NT.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nt.Backend_NT.filters.JwtRequestFilter;
import com.nt.Backend_NT.services.NTUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private NTUserDetailsService userDetailService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	private AuthenticationManager authenticationManager;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
	}

	@Bean
	public AuthenticationManager getAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
		return authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/session")
			.permitAll()
			.antMatchers("/user")
			.permitAll()
			.antMatchers("/v2/api-docs", 
					"/configuration/ui", 
					"/swagger-resources",
					"/configuration/security",
					"/swagger-ui.html", "/webjars/**", 
					"/swagger-resources/configuration/ui", 
					"/swagge‌​r-ui.html", 
					"/swagger-resources/configuration/security",
					"/swagger-ui/**",
					"/swagger.json")
			.permitAll()
			.anyRequest().authenticated()
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}