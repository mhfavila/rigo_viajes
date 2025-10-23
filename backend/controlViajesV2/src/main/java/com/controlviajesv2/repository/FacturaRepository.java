package com.controlviajesv2.repository;

import com.controlviajesv2.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {





    /* esto son posibles metodos a futuro para un filtro*/
    // Buscar facturas por empresa
    List<Factura> findByEmpresaId(Long empresaId);

    // Buscar facturas por usuario
    List<Factura> findByUsuarioId(Long usuarioId);

    // Buscar factura por número de factura (único)
    Factura findByNumeroFactura(String numeroFactura);

    // Buscar facturas por estado
    List<Factura> findByEstado(String estado);


    //metodo para los datos de prueba que verifica que solo haya una factura con el numero de factura
    boolean existsByNumeroFactura(String numero);
}



