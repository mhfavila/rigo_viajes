package com.rigoV2.controlViajesV2.controller;

import com.rigoV2.controlViajesV2.dto.ViajeDTO;
import com.rigoV2.controlViajesV2.service.ViajeService;

import com.rigoV2.controlViajesV2.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de viajes.
 * Permite realizar operaciones CRUD sobre la entidad Viaje.
 */
@RestController
@RequestMapping(AppConstants.REQUEST_VIAJECONTROLLER)
public class ViajeController {

    private static final Logger logger = LoggerFactory.getLogger(ViajeController.class);

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    /**
     * Obtiene todos los viajes registrados.
     */
    @GetMapping
    public ResponseEntity<List<ViajeDTO>> listarViajes() {
        logger.info("Listando todos los viajes");
        List<ViajeDTO> viajes = viajeService.obtenerTodos();
        return ResponseEntity.ok(viajes);
    }

    /**
     * Obtiene un viaje por su ID.
     */
    @GetMapping(AppConstants.REQUEST_VIAJECONTROLLER_ID)
    public ResponseEntity<ViajeDTO> obtenerViaje(@PathVariable Long id) {
        logger.info("Obteniendo viaje con ID: {}", id);
        ViajeDTO viaje = viajeService.obtenerPorId(id);
        return ResponseEntity.ok(viaje);
    }

    /**
     * Crea un nuevo viaje.
     */
    @PostMapping
    public ResponseEntity<ViajeDTO> crearViaje(@Valid @RequestBody ViajeDTO viajeDTO) {
        logger.info("Creando nuevo viaje con destino: {}", viajeDTO.getDestino());
        ViajeDTO creado = viajeService.crear(viajeDTO);
        return ResponseEntity.ok(creado);
    }

    /**
     * Actualiza un viaje existente.
     */
    @PutMapping(AppConstants.REQUEST_VIAJECONTROLLER_ID)
    public ResponseEntity<ViajeDTO> actualizarViaje(
            @PathVariable Long id,
            @Valid @RequestBody ViajeDTO viajeDTO) {
        logger.info("Actualizando viaje con ID: {}", id);
        ViajeDTO actualizado = viajeService.actualizar(id, viajeDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un viaje por su ID.
     */
    @DeleteMapping(AppConstants.REQUEST_VIAJECONTROLLER_ID)
    public ResponseEntity<Void> eliminarViaje(@PathVariable Long id) {
        logger.info("Eliminando viaje con ID: {}", id);
        viajeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}