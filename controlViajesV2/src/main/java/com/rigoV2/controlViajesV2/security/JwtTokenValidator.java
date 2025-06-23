package com.rigoV2.controlViajesV2.security;

import com.rigoV2.controlViajesV2.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenValidator {

    private final JwtProperties jwtProperties;
    private final JwtTokenParser tokenParser;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenValidator.class);

    public JwtTokenValidator(JwtProperties jwtProperties, JwtTokenParser tokenParser) {
        this.jwtProperties = jwtProperties;
        this.tokenParser = tokenParser;
    }

    /**
     * Valida el token comparando el username y su expiración
     */
    public boolean validateToken(String token, String username) {
        logger.debug("Validando token para el usuario: {}", username);
        try {
            final String tokenUsername = tokenParser.extractUsername(token);
            if (!tokenUsername.equals(username)) {
                logger.warn("El username del token no coincide. Token: {}, Usuario esperado: {}", tokenUsername, username);
                return false;
            }
            if (isTokenExpired(token)) {
                logger.warn("El token ha expirado para el usuario: {}", username);
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Token inválido para el usuario {}: {}", username, e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = tokenParser.extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /**
     * Devuelve la clave secreta como objeto SecretKey
     */
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
