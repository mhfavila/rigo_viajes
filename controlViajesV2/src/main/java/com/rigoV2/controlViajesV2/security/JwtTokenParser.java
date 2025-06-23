package com.rigoV2.controlViajesV2.security;

import com.rigoV2.controlViajesV2.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.function.Function;

@Component
public class JwtTokenParser {

    private final JwtProperties jwtProperties;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenParser.class);

    public JwtTokenParser(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Extrae nombre de usuario del token
    public String extractUsername(String token) throws JwtException {
        logger.debug("Extrayendo username del token JWT");
        return extractClaim(token, Claims::getSubject);
    }
    // Extrae cualquier claim con una función
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException{
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims parseClaims(String token) throws JwtException {
        logger.debug("Parseando claims del token JWT");
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
