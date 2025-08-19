package com.controlviajesv2.repository;

import com.controlviajesv2.dto.ViajeDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByEmpresa(Empresa empresa);
    List<Viaje> findByEmpresaId(Long empresaId);
}
