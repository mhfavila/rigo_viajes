package com.controlviajesv2.repository;

import com.controlviajesv2.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // Buscar todos los servicios de una factura específica
    List<Servicio> findByFacturaId(Long facturaId);

    // Buscar servicios por tipo
    List<Servicio> findByTipoServicio(String tipoServicio);

    // Ejemplo: buscar servicios de una factura ordenados por orden
    List<Servicio> findByFacturaIdOrderByOrdenAsc(Long facturaId);


}


