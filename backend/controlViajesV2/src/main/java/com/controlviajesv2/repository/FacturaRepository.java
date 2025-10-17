package com.controlviajesv2.repository;

import com.controlviajesv2.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    // Buscar facturas por empresa
    List<Factura> findByEmpresaId(Long empresaId);

    // Buscar facturas por usuario
    List<Factura> findByUsuarioId(Long usuarioId);

    // Buscar factura por número de factura (único)
    Factura findByNumeroFactura(String numeroFactura);

    // Buscar facturas por estado
    List<Factura> findByEstado(String estado);

}



