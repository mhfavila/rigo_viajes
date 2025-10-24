package com.controlviajesv2.service;


import com.controlviajesv2.dto.FacturaDTO;

import java.util.List;

public interface FacturaService {

    // Crea una nueva factura en el sistema
       FacturaDTO crearFactura(FacturaDTO facturaDTO);

    // Devuelve todas las facturas registradas
    List<FacturaDTO> listarFacturas();

    // Busca una factura por su ID
    FacturaDTO obtenerFacturaPorId(Long id);

    // Actualiza los datos de una factura existente
    FacturaDTO actualizarFactura(Long id, FacturaDTO facturaDTO);

    // Elimina una factura por su ID
    void eliminarFactura(Long id);

//busca las facturas de una empresa, busca por él, id de la empresa
    List<FacturaDTO> getFacturasPorEmpresa(Long empresaId);
}
