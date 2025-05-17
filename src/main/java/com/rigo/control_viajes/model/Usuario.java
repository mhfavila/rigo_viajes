package com.rigo.control_viajes.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * Entidad que representa un usuario registrado en la aplicación.
 * Implementa la interfaz UserDetails de Spring Security para integrar la autenticación.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Será el identificador único (puede ser email o nombre de usuario)

    @Column(nullable = false)
    private String password;

    // Roles o permisos del usuario (para esta fase simple, lo dejamos básico)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Sin roles por ahora
    }

    // A continuación se implementan métodos obligatorios de UserDetails.
    @Override
    public boolean isAccountNonExpired() {
        return true; // No se gestiona expiración de cuentas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // No se gestiona bloqueo de cuentas
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // No se gestiona expiración de credenciales
    }

    @Override
    public boolean isEnabled() {
        return true; // Todos los usuarios están habilitados por defecto
    }
}