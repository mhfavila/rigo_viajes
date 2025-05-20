package com.rigoV2.controlViajesV2.repository;

import com.rigoV2.controlViajesV2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByNombre(String nombre);
}
