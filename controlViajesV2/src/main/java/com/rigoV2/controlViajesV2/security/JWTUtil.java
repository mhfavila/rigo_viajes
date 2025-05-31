package com.rigoV2.controlViajesV2.security;


import com.rigoV2.controlViajesV2.security.config.JwtProperties;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {
    //private final String SECRET_KEY = "mi_clave_secreta_muy_segura_12345"; // Cambia por algo seguro y en config

    //private final long JWT_EXPIRATION_MS = 86400000; // 1 día en milisegundos
    private final JwtProperties jwtProperties;

    public JWTUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Genera token con el nombre del usuario
    public String generateToken(String username) throws JwtException {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    // Extrae nombre de usuario del token
    public String extractUsername(String token) throws JwtException {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae cualquier claim con una función
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException{
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    // Valida token contra username y fecha expiración
    public boolean validateToken(String token, String username) throws JwtException{
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Revisa si el token expiró
    private boolean isTokenExpired(String token) throws JwtException {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private Claims parseClaims(String token) throws JwtException {

            return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();

    }


}
