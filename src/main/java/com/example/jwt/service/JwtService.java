package com.example.jwt.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private static final String SECRATE_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

	public String extactUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}
	
	
	public <T> T extractClaims (String token , Function<Claims ,T> claimResolver) {
		//TODO : see what type of data comes in this function
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	
	//Method to Generate Token without claims 
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(),userDetails);
	}
	
	
	
	//Method to Generate Token with Claims
	public String generateToken(
			Map<String, Object> extractClaims,
			UserDetails userDetails
			) { 
		return Jwts
				.builder()
				.setClaims(extractClaims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
		
	}
	
	
	//Method to Validate token
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extactUsername(token);
		return (userName.equals(userDetails.getUsername())) && 	!isTokenExpired(token);
	}
	
	//Checks if the token is expired 
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	//Extracts Expiration Date
	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}


	//

	//Extract Claims form the token
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
				
	}


	private Key getSignInKey() {
		byte[]  keyBytes = Decoders.BASE64.decode(SECRATE_KEY);
		System.out.println("KeyBytes --> " + keyBytes);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}


