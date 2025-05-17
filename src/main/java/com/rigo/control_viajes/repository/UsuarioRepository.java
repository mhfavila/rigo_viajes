package com.rigo.control_viajes.repository;

import com.rigo.control_viajes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Repositorio JPA para la entidad Usuario.
 *
 * Proporciona métodos CRUD y una consulta personalizada para encontrar usuarios por su nombre de usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario que se desea buscar.
     * @return Un Optional que contiene el usuario si fue encontrado, o vacío si no.
     */
    Optional<Usuario> findByUsername(String username);

}
