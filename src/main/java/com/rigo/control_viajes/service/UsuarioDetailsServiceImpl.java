package com.rigo.control_viajes.service;

import com.rigo.control_viajes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementación personalizada de UserDetailsService.
 *
 * Se encarga de cargar los detalles del usuario desde la base de datos
 * utilizando el nombre de usuario para la autenticación.
 */
@Service
@Lazy
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor que inyecta el repositorio de usuarios.
     *
     * @param usuarioRepository Repositorio para acceder a los datos de los usuarios.
     */
    @Autowired
    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga un usuario desde la base de datos por su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return El objeto UserDetails que representa al usuario autenticado.
     * @throws UsernameNotFoundException Si no se encuentra el usuario.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
    }
}
