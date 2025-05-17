package com.rigo.control_viajes.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final JWTUtil jwtUtil;
    private final @Lazy UserDetailsService userDetailsService;

    public JWTAuthenticationFilter(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            logger.debug("Encabezado Authorization recibido: {}", authorizationHeader);
            logger.debug("Token JWT recibido: {}", token);
            try {
                username = jwtUtil.getUsernameFromToken(token);
                logger.debug("Username extraído del token: {}", username);
            } catch (Exception e) {
                logger.warn("Error al extraer username del token JWT: {}", e.getMessage());
            }
        } else {
            logger.debug("No se encontró el encabezado Authorization con formato Bearer.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.debug("Detalles del usuario cargados: {}", userDetails.getUsername());

            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                logger.info("Token JWT válido. Autenticando al usuario: {}", username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warn("Token JWT inválido o expirado para el usuario: {}", username);
            }
        } else if (username != null) {
            logger.debug("Ya existe una autenticación en el contexto de seguridad para: {}", username);
        }

        filterChain.doFilter(request, response);
    }
}