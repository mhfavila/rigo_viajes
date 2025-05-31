package com.rigoV2.controlViajesV2.repository;

import com.rigoV2.controlViajesV2.entity.Empresa;
import com.rigoV2.controlViajesV2.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByEmpresa(Empresa empresa);
}
