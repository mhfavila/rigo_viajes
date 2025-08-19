package com.controlviajesv2.security;


import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.security.config.JwtProperties;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenGenerator {
    @Autowired
   private JwtProperties jwtProperties;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenGenerator.class);

    public JwtTokenGenerator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    // Genera token con el nombre del usuario
    public  String generateToken(Usuario usuario) throws JwtException {
        logger.debug("Generando token para el usuario: {}", usuario.getNombre());
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(usuario.getNombre())
                .claim("usuarioId", usuario.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }
}
