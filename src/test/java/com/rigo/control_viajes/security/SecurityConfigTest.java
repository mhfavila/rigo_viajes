package com.rigo.control_viajes.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class SecurityConfigTest {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()        // Desactivar CSRF para tests
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Permitir todas las peticiones sin autenticación
                );

        return http.build();
    }
}
