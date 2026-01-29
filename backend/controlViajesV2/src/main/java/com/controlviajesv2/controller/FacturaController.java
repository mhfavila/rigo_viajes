package com.controlviajesv2.controller;



import com.controlviajesv2.dto.FacturaDTO;
import com.controlviajesv2.service.FacturaService;
import com.controlviajesv2.serviceImpl.pdf.FacturaPdfService;
import com.controlviajesv2.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstants.REQUEST_FACTURACONTROLLER)
@Tag(name = "Facturas", description = "Gestión de facturas y generación de PDF")
public class FacturaController {

    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    private final FacturaService facturaService;
    private final FacturaPdfService facturaPdfService;

    public FacturaController(FacturaService facturaService, FacturaPdfService facturaPdfService) {
        this.facturaService = facturaService;
        this.facturaPdfService = facturaPdfService;
    }

    /**
     * Devuelve la lista de todas las facturas.
     */
    @Operation(summary = "Listar todas las facturas", description = "Devuelve el listado completo de facturas registradas en el sistema.")
    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listarFacturas() {
        logger.info("Solicitando listado de todas las facturas");
        List<FacturaDTO> facturas = facturaService.listarFacturas();
        return ResponseEntity.ok(facturas);
    }

    /**
     * Devuelve una factura por su ID.
     */
    @Operation(summary = "Obtener factura por ID", description = "Busca una factura específica por su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping(AppConstants.REQUEST_FACTURACONTROLLER_ID)
    public ResponseEntity<FacturaDTO> obtenerFactura(@PathVariable Long id) {
        logger.info("Buscando factura con ID: {}", id);
        FacturaDTO factura = facturaService.obtenerFacturaPorId(id);
        return ResponseEntity.ok(factura);
    }

    /**
     * Crea una nueva factura a partir de un DTO válido.
     */
    @Operation(summary = "Crear nueva factura", description = "Genera una factura nueva validando los datos de entrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de factura inválidos")
    })
    @PostMapping

    public ResponseEntity<FacturaDTO> crearFactura(@Valid @RequestBody FacturaDTO facturaDTO) {
        logger.info("Creando factura número: {}", facturaDTO.getNumeroFactura());
        FacturaDTO nueva = facturaService.crearFactura(facturaDTO);
        return ResponseEntity.ok(nueva);
    }

    /**
     * Actualiza una factura existente.
     */
    @Operation(summary = "Actualizar factura", description = "Modifica los datos de una factura existente.")
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
    @Operation(summary = "Eliminar factura", description = "Elimina una factura del sistema.")
    @DeleteMapping(AppConstants.REQUEST_FACTURACONTROLLER_ID)
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        logger.info("Eliminando factura con ID: {}", id);
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Devuelve todas las facturas de una empresa concreta
     * @param empresaId id de la empresa
     * @return lista de facturas
     */
    @Operation(summary = "Listar facturas por Empresa", description = "Obtiene todas las facturas asociadas a una empresa concreta.")
    @GetMapping(AppConstants.REQUEST_FACTURAS_EMPRESA_ID)
    public List<FacturaDTO> getFacturasPorEmpresa(@PathVariable("empresaId") Long empresaId) {
        return facturaService.getFacturasPorEmpresa(empresaId);
    }

    /**
     * Genera y devuelve el PDF de una factura concreta
     * @param id el id de la factura
     * @return
     */
    @Operation(summary = "Descargar PDF de Factura", description = "Genera y descarga el archivo PDF de la factura solicitada.")
    @GetMapping(value = AppConstants.REQUEST_FACTURAS_PDF_ID, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarFacturaPdf(@PathVariable Long id) {
        try {
            byte[] pdf = facturaPdfService.generarPdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=factura_" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}