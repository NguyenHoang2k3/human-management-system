package com.lab.server.configs.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.access-token.expiration}")
	private long expirationTime;
	@Value("${jwt.refresh-token.expiration}")
	private long expirationRefreshTime;
	@Getter
    @Value("${jwt.secret-key}")
	private String secretKey;
	
	private final String AUTH_PREFIX = "Bearer ";
	private final String HEADER = "Authorization";

	public String generateToken(String username){
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationTime);
		
		return Jwts.builder().setHeader(Map.of("typ", "JWT"))
				.setSubject(username)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	public String getUsernameFromToken(String token){
		Claims claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();

		return claims.getSubject();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}

	public String getJwtFromRequest(HttpServletRequest request){
		String bearerToken = request.getHeader(HEADER);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTH_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	public long getExpirationTime() {
		return this.expirationTime;
	}

}
