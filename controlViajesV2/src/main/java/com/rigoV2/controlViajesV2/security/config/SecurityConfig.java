package com.rigoV2.controlViajesV2.security.config;

import com.rigoV2.controlViajesV2.dto.AuthController;
import com.rigoV2.controlViajesV2.security.CustomAccessDeniedHandler;
import com.rigoV2.controlViajesV2.security.CustomAuthenticationEntryPoint;
import com.rigoV2.controlViajesV2.security.JWTAuthenticationFilter;
import com.rigoV2.controlViajesV2.security.UserDetailsServiceImpl;
import com.rigoV2.controlViajesV2.util.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);


    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, UserDetailsServiceImpl userDetailsService, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configurando el SecurityFilterChain...");
        logger.debug("Se permite acceso libre a /api/auth/**");
        logger.debug("JWTAuthenticationFilter agregado antes de UsernamePasswordAuthenticationFilter");
        logger.debug("AuthenticationEntryPoint: {}", customAuthenticationEntryPoint.getClass().getSimpleName());
        logger.debug("AccessDeniedHandler: {}", customAccessDeniedHandler.getClass().getSimpleName());
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AppConstants.API_AUTH_PATH).permitAll() // permisos a login y registro
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // sin sesión
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)//maneja 401 no autenticado
                        .accessDeniedHandler(customAccessDeniedHandler)  // Maneja 403 acceso denegado
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.info("Configurando DaoAuthenticationProvider con UserDetailsServiceImpl y BCryptPasswordEncoder");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("Obteniendo AuthenticationManager desde AuthenticationConfiguration");
        return config.getAuthenticationManager();
    }
}
