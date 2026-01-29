package com.controlviajesv2.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Rigo Viajes",
                version = "1.0",
                description = "Documentación de la API para la gestión de viajes, empresas y facturas.",
                /*contact = @Contact(
                        name = "Favila",
                        url = "https://www.linkedin.com/in/tu-perfil",
                        email = "favila@ejemplo.com"
                ),*/
                license = @License(
                        name = "Standard License"
                        //url = "https://www.tuweb.com"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Autenticación JWT. Introduce el token así: Bearer eyJhbG...",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}