package com.example.jwt.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwt.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	
	private final JwtService jwtService;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		//Extract the Authorization Header from request
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		
		//Check if the header is null or it does not start with 'Bearer ' 
		if(authHeader == null || ! authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//Extracting jwt token from the header, form index 7 as jwt-token is after "Bearer "
		jwt = authHeader.substring(7);
		
		
		//Extract UserEmail(or userName in some case) from jwt-token 
		
		userEmail = jwtService.extactUsername(jwt);
		
		
		
		
	}

}
