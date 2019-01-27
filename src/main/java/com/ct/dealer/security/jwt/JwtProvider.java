package com.ct.dealer.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ct.dealer.security.services.UserPrinciple;

import java.util.Date;

@Component
public class JwtProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

	@Value( "${grokonez.app.jwtSecret}" )
	private String ctSecret;

	@Value( "${grokonez.app.jwtExpiration}" )
	private int ctTimeout;

	public String generateJwtToken( Authentication authentication ) {
		UserPrinciple userPrincipal = (UserPrinciple)authentication.getPrincipal();
		return Jwts.builder().setSubject( (userPrincipal.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date( (new Date()).getTime() + ctTimeout * 1000))
				.signWith(SignatureAlgorithm.HS512, ctSecret).compact();
	}

	public boolean validateJwtToken( String authToken ) {
		try {
			Jwts.parser().setSigningKey(ctSecret).parseClaimsJws(authToken);
			return true;
		} catch( SignatureException e ) {
			logger.error("Invalid JWT signature -> Message: {} ", e);
		} catch( MalformedJwtException e ) {
			logger.error("Invalid JWT token -> Message: {}", e);
		} catch( ExpiredJwtException e ) {
			logger.error("Expired JWT token -> Message: {}", e);
		} catch( UnsupportedJwtException e ) {
			logger.error("Unsupported JWT token -> Message: {}", e);
		} catch( IllegalArgumentException e ) {
			logger.error("JWT claims string is empty -> Message: {}", e);
		}

		return false;
	}

	public String getUserNameFromJwtToken( String token ) {
		return Jwts.parser().setSigningKey(ctSecret).parseClaimsJws(token).getBody().getSubject();
	}
}