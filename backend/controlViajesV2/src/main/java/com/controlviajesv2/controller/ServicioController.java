package com.controlviajesv2.controller;

import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.service.ServicioService;
import com.controlviajesv2.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Servicio.
 */
@RestController
@RequestMapping(AppConstants.REQUEST_SERVICIOCONTROLLER)
public class ServicioController {

    private static final Logger logger = LoggerFactory.getLogger(ServicioController.class);
    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listarServicios() {
        logger.info("Solicitando listado de servicios");
        return ResponseEntity.ok(servicioService.listarServicios());
    }

    @GetMapping(AppConstants.REQUEST_SERVICIOCONTROLLER_ID)
    public ResponseEntity<ServicioDTO> obtenerServicio(@PathVariable Long id) {
        logger.info("Buscando servicio con ID: {}", id);
        return ResponseEntity.ok(servicioService.obtenerServicioPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServicioDTO> crearServicio(@RequestBody ServicioDTO servicioDTO) {
        logger.info("Creando servicio tipo: {}", servicioDTO.getTipoServicio());
        return ResponseEntity.ok(servicioService.crearServicio(servicioDTO));
    }

    @PutMapping(AppConstants.REQUEST_SERVICIOCONTROLLER_ID)
    public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable Long id, @RequestBody ServicioDTO servicioDTO) {
        logger.info("Actualizando servicio con ID: {}", id);
        return ResponseEntity.ok(servicioService.actualizarServicio(id, servicioDTO));
    }

    @DeleteMapping(AppConstants.REQUEST_SERVICIOCONTROLLER_ID)
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        logger.info("Eliminando servicio con ID: {}", id);
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}