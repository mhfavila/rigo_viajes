package com.rigo.control_viajes.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Clase utilitaria para generar y validar tokens JWT.
 */
@Component
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Clave secreta en formato Key (objeto seguro) para jjwt 0.11.x
    private Key key;

    // Inicializar la key después de inyectar la propiedad 'secret'
    @PostConstruct
    public void init() {
        // Convierte la cadena secreta a Key, con codificación UTF-8
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT para un usuario dado.
     *
     * @param username Nombre de usuario para incluir en el token.
     * @return Token JWT firmado.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        logger.info("Generando token JWT para el usuario: {}", username);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        logger.debug("Token JWT generado: {}", token);

        return token;
    }

    /**
     * Extrae cualquier claim del token usando una función.
     *
     * @param token Token JWT.
     * @param claimsResolver Función para extraer un claim.
     * @param <T> Tipo del claim a extraer.
     * @return El claim extraído.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.warn("Error al obtener claim del token: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene todos los claims del token, validando la firma con la clave secreta.
     *
     * @param token Token JWT.
     * @return Claims decodificados.
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            logger.debug("Validando token y extrayendo claims.");
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            logger.error("Error al analizar el token JWT: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene el username (subject) del token.
     *
     * @param token Token JWT.
     * @return Username contenido en el token.
     */
    public String getUsernameFromToken(String token) {
        logger.debug("Extrayendo username del token.");
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Verifica si el token ha expirado.
     *
     * @param token Token JWT.
     * @return true si el token está expirado, false si sigue válido.
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaimFromToken(token, Claims::getExpiration);
            boolean expired = expiration.before(new Date());
            logger.debug("¿Token expirado?: {}", expired);
            return expired;
        } catch (Exception e) {
            logger.warn("No se pudo determinar si el token está expirado: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Valida el token comparando el username y verificando que no haya expirado.
     *
     * @param token Token JWT.
     * @param username Nombre de usuario esperado.
     * @return true si el token es válido.
     */
    public boolean validateToken(String token, String username) {
        try {
            final String tokenUsername = getUsernameFromToken(token);
            boolean valid = tokenUsername.equals(username) && !isTokenExpired(token);
            logger.info("¿Token válido para {}?: {}", username, valid);
            return valid;
        } catch (Exception e) {
            logger.warn("Error al validar el token JWT: {}", e.getMessage());
            return false;
        }
    }
}