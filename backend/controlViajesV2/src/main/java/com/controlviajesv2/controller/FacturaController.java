package com.controlviajesv2.controller;



import com.controlviajesv2.dto.FacturaDTO;
import com.controlviajesv2.service.FacturaService;
import com.controlviajesv2.util.AppConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstants.REQUEST_FACTURACONTROLLER)
public class FacturaController {

    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /**
     * Devuelve la lista de todas las facturas.
     */
    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarFacturas() {
        logger.info("Solicitando listado de todas las facturas");
        List<FacturaDTO> facturas = facturaService.listarFacturas();
        return ResponseEntity.ok(facturas);
    }

    /**
     * Devuelve una factura por su ID.
     */
    @GetMapping(AppConstants.REQUEST_FACTURACONTROLLER_ID)
    public ResponseEntity<FacturaDTO> obtenerFactura(@PathVariable Long id) {
        logger.info("Buscando factura con ID: {}", id);
        FacturaDTO factura = facturaService.obtenerFacturaPorId(id);
        return ResponseEntity.ok(factura);
    }

    /**
     * Crea una nueva factura a partir de un DTO válido.
     */
    @PostMapping
    public ResponseEntity<FacturaDTO> crearFactura(@Valid @RequestBody FacturaDTO facturaDTO) {
        logger.info("Creando factura número: {}", facturaDTO.getNumeroFactura());
        FacturaDTO nueva = facturaService.crearFactura(facturaDTO);
        return ResponseEntity.ok(nueva);
    }

    /**
     * Actualiza una factura existente.
     */
    @PutMapping(AppConstants.REQUEST_FACTURACONTROLLER_ID)
    public ResponseEntity<FacturaDTO> actualizarFactura(
            @PathVariable Long id,
            @Valid @RequestBody FacturaDTO facturaDTO) {
        logger.info("Actualizando factura con ID: {}", id);
        FacturaDTO actualizada = facturaService.actualizarFactura(id, facturaDTO);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina una factura por su ID.
     */
    @DeleteMapping(AppConstants.REQUEST_FACTURACONTROLLER_ID)
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        logger.info("Eliminando factura con ID: {}", id);
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}