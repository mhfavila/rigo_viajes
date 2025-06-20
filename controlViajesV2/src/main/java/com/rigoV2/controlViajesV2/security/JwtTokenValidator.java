package com.rigoV2.controlViajesV2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenValidator {

    private final JwtTokenParser tokenParser;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenValidator.class);

    public JwtTokenValidator(JwtTokenParser tokenParser) {
        this.tokenParser = tokenParser;
    }

    // Valida token contra username y fecha expiración
    public boolean validateToken(String token, String username) throws JwtException {
        logger.debug("Validando token para el usuario: {}", username);
        final String tokenUsername = tokenParser.extractUsername(token);
        if (!tokenUsername.equals(username)) {
            logger.warn("El username del token no coincide. Token: {}, Usuario esperado: {}", tokenUsername, username);
        }
        if (isTokenExpired(token)) {
            logger.warn("El token ha expirado para el usuario: {}", username);
        }
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Revisa si el token expiró
    private boolean isTokenExpired(String token) throws JwtException {
        final Date expiration = tokenParser.extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
