package com.controlviajesv2.repository;

import com.controlviajesv2.entity.TokenRevocado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRevocadoRepository extends JpaRepository<TokenRevocado, Long> {
    Optional<TokenRevocado> findByToken(String token);
    void deleteByFechaExpiracionBefore(LocalDateTime now);
}