package com.foodbridge.security.filter;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.foodbridge.auth.jwt.JwtService;
import com.foodbridge.security.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;

	public JwtAuthenticationFilter(
	        JwtService jwtService,
	        CustomUserDetailsService userDetailsService) {

	    this.jwtService = jwtService;
	    this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		System.out.println("AUTH HEADER: " + authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		    System.out.println("NO BEARER TOKEN");
		    filterChain.doFilter(request, response);
		    return;
		}

		String jwt = authHeader.substring(7);

		System.out.println("JWT RECEIVED");

		String username = jwtService.extractUsername(jwt);

		System.out.println("JWT USERNAME: " + username);

		UserDetails userDetails =
		        userDetailsService.loadUserByUsername(username);

		System.out.println("USER FOUND: " + userDetails.getUsername());

		if (jwtService.isTokenValid(jwt, userDetails)) {

		    UsernamePasswordAuthenticationToken authentication =
		            new UsernamePasswordAuthenticationToken(
		                    userDetails,
		                    null,
		                    userDetails.getAuthorities()
		            );

		    SecurityContextHolder.getContext()
		            .setAuthentication(authentication);

		    System.out.println("JWT AUTHENTICATION SUCCESS");
		}

		filterChain.doFilter(request, response);

		
	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

	    String path = request.getServletPath();

	    return path.equals("/api/users/register")
	            || path.equals("/api/auth/login")
	            || path.equals("/api/auth/verify-otp");
	}
	
	

}