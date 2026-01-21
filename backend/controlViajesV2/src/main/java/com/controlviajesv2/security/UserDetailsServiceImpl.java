package com.controlviajesv2.security;

import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Método que Spring Security usa para cargar el usuario desde la BD según el username (nombre)
     * @param email el nombre de usuario que se quiere autenticar
     * @return UserDetails que contiene datos del usuario, password y roles
     * @throws UsernameNotFoundException si no existe el usuario en la BD
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Intentando cargar usuario con email: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {logger.warn("Usuario no encontrado: {}", email); return new UsernameNotFoundException("Usuario no encontrado: " + email);});
        logger.info("Usuario cargado exitosamente: {}", email);
        return new UsuarioPrincipal(usuario);  // UsuarioPrincipal implementa UserDetails
    }
}
