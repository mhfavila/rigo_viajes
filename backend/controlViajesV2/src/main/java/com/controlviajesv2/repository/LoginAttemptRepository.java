package com.controlviajesv2.repository;

import com.controlviajesv2.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    Optional<LoginAttempt> findByClave(String clave);
    void deleteByClave(String clave);

    // Para limpiar registros viejos automáticamente
    void deleteByUltimaModificacionBefore(LocalDateTime fechaLimite);
}