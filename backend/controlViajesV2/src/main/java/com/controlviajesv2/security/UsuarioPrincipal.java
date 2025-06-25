package com.controlviajesv2.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.controlviajesv2.entity.Usuario;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UsuarioPrincipal implements UserDetails{


    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> roles = usuario.getRoles();
        return roles.stream()
                .map(SimpleGrantedAuthority::new) // Ej: "ROLE_USER", "ROLE_ADMIN"
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getNombre(); // o .getEmail() si usas email para login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // puedes implementar lógica según tu app
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // puedes bloquear usuarios si lo deseas
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // o implementar expiración de credenciales
    }

    @Override
    public boolean isEnabled() {
        return true; // puedes controlar si un usuario está activo o no
    }
}
