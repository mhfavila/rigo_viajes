package com.controlviajesv2.security;


import com.controlviajesv2.security.service.TokenBlacklistService;
import com.controlviajesv2.serviceImpl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final JwtTokenValidator jwtTokenValidator;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    @Autowired
    private JwtTokenParser jwtTokenParser;

    public JWTAuthenticationFilter(JwtTokenValidator jwtTokenValidator, UserDetailsServiceImpl userDetailsService, TokenBlacklistService tokenBlacklistService) {
        this.jwtTokenValidator = jwtTokenValidator;

        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Filtro JWT ejecutándose. Header Authorization: {}", request.getHeader("Authorization"));

        logger.debug("Procesando autenticación para la petición: {} {}", request.getMethod(), request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // El header debe comenzar con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            jwt = authHeader.substring(7); // extrae el token sin "Bearer "

            // Comprobamos si el token ha sido revocado antes de intentar procesarlo
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                logger.warn("Intento de acceso con token revocado (Blacklisted)");
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "El token ha sido revocado (Cierre de sesión previo)", request.getRequestURI());
                return; // Cortamos la ejecución aquí
            }
            try {
                username = jwtTokenParser.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("Token expirado: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "El token ha expirado",request.getRequestURI());
                return;
            } catch (MalformedJwtException e) {
                logger.warn("Token mal formado: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Token mal formado",request.getRequestURI());
                return;
            } catch (SignatureException e) {
                logger.warn("Firma del token inválida: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Firma del token inválida",request.getRequestURI());
                return;
            } catch (Exception e) {
                logger.error("Error al procesar el token: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno de autenticación",request.getRequestURI());
                return;
            }


        }else {
            logger.debug("No se encontró un token JWT en la cabecera Authorization");
        }


        // Si username está y no hay autenticación previa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenValidator.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Autenticación JWT exitosa para el usuario: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message, String ruta) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");


        Map<String,Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", Instant.now().toString());
        errorBody.put("status",status);
        errorBody.put("error",message);
        errorBody.put("ruta",ruta);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorBody));
    }
}
