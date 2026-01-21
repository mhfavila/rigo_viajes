package com.controlviajesv2.repository;

import com.controlviajesv2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombre(String nombre);//esto era para cuando el login era por nombre y no por email

    boolean existsByNombre(String nombre);
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email); //
}
