package com.controlviajesv2.security.config;

import com.controlviajesv2.security.CustomAccessDeniedHandler;
import com.controlviajesv2.security.CustomAuthenticationEntryPoint;
import com.controlviajesv2.security.JWTAuthenticationFilter;
import com.controlviajesv2.security.UserDetailsServiceImpl;
import com.controlviajesv2.util.AppConstants;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Lee "cors.allowed-origins" de application.properties Si no existe, usa "http://localhost:4200" por defecto.
    @Value("${cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    //Controla qué pasa cuando un usuario no está autenticado -> genera error 401 Unauthorized.
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    //Controla qué pasa cuando un usuario sí está autenticado pero no tiene permisos -> genera error 403
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    //Carga un usuario desde la base de datos mediante UserDetailsService
    private final UserDetailsServiceImpl userDetailsService;
    //lee el token JWT del header ->valida el token ->mete el usuario autenticado en el contexto de Spring
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            UserDetailsServiceImpl userDetailsService,
            JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configurando el SecurityFilterChain...");

        http
                // Configuración CORS centralizada -> activa CORS ,llama al metodo corsConfigurationSource() para obtener los origenes permitidos
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Deshabilitamos CSRF (usamos JWT)
                .csrf(csrf -> csrf.disable())

                // Configuración de autorización -> se puede usar sin estar logueaso :loguin y registro
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AppConstants.API_AUTH_PATH).permitAll()
                        .anyRequest().authenticated()
                )

                // Sin sesiones (STATELESS) ,cada peticion debe tener su token JWWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Proveedor de autenticación
                .authenticationProvider(authenticationProvider())

                // Manejo de excepciones ,maneja los errores 401 y 403
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // Filtro JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        logger.debug("SecurityFilterChain configurado correctamente");
        return http.build();
    }

    /**
     *  Configuración CORS centralizada y mejorada
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("Configurando CORS...");

        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos
                    //Convertimos el String "http://localhost:4200,https://miweb.com" en una lista separando por comas.

        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        logger.info("Orígenes permitidos cargados: {}", configuration.getAllowedOrigins());

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        // Headers específicos permitidos (más seguro que "*")
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With"
        ));

        // Headers que el navegador puede leer
        configuration.setExposedHeaders(List.of(
                "Authorization"
        ));

        // Permitir credenciales (necesario para JWT en cookies o headers)
        configuration.setAllowCredentials(true);

        // Tiempo de cache de la configuración CORS
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        logger.debug("CORS configurado para orígenes: {}", configuration.getAllowedOrigins());
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        logger.info("Configurando DaoAuthenticationProvider");
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        logger.info("Obteniendo AuthenticationManager");
        return config.getAuthenticationManager();
    }
}
