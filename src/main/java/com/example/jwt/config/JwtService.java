package com.example.jwt.config;

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

	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(),userDetails);
	}
	
	
	//Generate token with extra claims
	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
			) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 *60 * 24))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	/*---------------------------------Check the Validity of token START ----------------------------------------------*/
	//1
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUserName(token);
		return (username.equals(userDetails.getUsername()) && isTokenExpired(token)  );
	}
	
	//2
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	//3
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	//4
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	/*---------------------------------Check the Validity of token END --------------------------------------------------------------*/

	
	/*---------------------------------Extract Claims form the token START --------------------------------------------------------------*/

	public <T> T extractClaim(String token,Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
					
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRATE_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	/*---------------------------------Extract Claims form the token END--------------------------------------------------------------*/

}
