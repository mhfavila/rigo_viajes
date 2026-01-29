package com.controlviajesv2.controller;

import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.service.ServicioService;
import com.controlviajesv2.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Servicio.
 */
@RestController
@RequestMapping(AppConstants.REQUEST_SERVICIOCONTROLLER)
@Tag(name = "Servicios", description = "Gestión de viajes y servicios realizados")
public class ServicioController {

    private static final Logger logger = LoggerFactory.getLogger(ServicioController.class);
    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los servicios", description = "Devuelve el listado completo de servicios registrados.")
    public ResponseEntity<List<ServicioDTO>> listarServicios() {
        logger.info("Solicitando listado de servicios");
        return ResponseEntity.ok(servicioService.listarServicios());
    }
    @Operation(summary = "Obtener servicio por ID", description = "Busca un servicio específico por su identificador.")
    @GetMapping(AppConstants.REQUEST_SERVICIOCONTROLLER_ID)
    public ResponseEntity<ServicioDTO> obtenerServicio(@PathVariable Long id) {
        logger.info("Buscando servicio con ID: {}", id);
        return ResponseEntity.ok(servicioService.obtenerServicioPorId(id));
    }
    @Operation(summary = "Crear nuevo servicio", description = "Registra un nuevo servicio realizado.")
    @PostMapping
    public ResponseEntity<ServicioDTO> crearServicio(@RequestBody ServicioDTO servicioDTO) {
        logger.info("Creando servicio tipo: {}", servicioDTO.getTipoServicio());
        return ResponseEntity.ok(servicioService.crearServicio(servicioDTO));
    }
    @Operation(summary = "Actualizar servicio", description = "Modifica los datos de un servicio existente.")
    @PutMapping(AppConstants.REQUEST_SERVICIOCONTROLLER_ID)
    public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable Long id, @RequestBody ServicioDTO servicioDTO) {
        logger.info("Actualizando servicio con ID: {}", id);
        return ResponseEntity.ok(servicioService.actualizarServicio(id, servicioDTO));
    }
    @Operation(summary = "Eliminar servicio", description = "Elimina un servicio del sistema.")
    @DeleteMapping(AppConstants.REQUEST_SERVICIOCONTROLLER_ID)
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        logger.info("Eliminando servicio con ID: {}", id);
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * metodo encargado de devolver los servicios de una empresa
     * @param empresaId
     * @return
     */

    @Operation(summary = "Listar servicios por Empresa", description = "Devuelve todos los servicios asociados a una empresa concreta.")
    @GetMapping(AppConstants.REQUEST_SERVICIOCONTROLLER_IDEMPRESA)
    public List<ServicioDTO> getServiciossPorEmpresa(@PathVariable Long empresaId) {
        return servicioService.findByEmpresaId(empresaId);
    }
}