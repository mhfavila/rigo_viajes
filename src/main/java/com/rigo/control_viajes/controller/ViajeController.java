package com.rigo.control_viajes.controller;

import com.rigo.control_viajes.DTO.ViajeDTO;
import com.rigo.control_viajes.mapper.ViajeMapper;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.model.Viaje;
import com.rigo.control_viajes.repository.EmpresaRepository;
import com.rigo.control_viajes.repository.ViajeRepository;
import com.rigo.control_viajes.service.ViajeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Marca la clase como un controlador REST
@RequestMapping("/api/viajes")  // Define la URL base para las peticiones de viajes
public class ViajeController {
    private static final Logger logger = LoggerFactory.getLogger(ViajeController.class);

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @PostMapping
    public ResponseEntity<?> crearViaje(@Valid @RequestBody ViajeDTO viajeDTO) {
        Viaje viajeCreado = viajeService.crearViaje(viajeDTO);
        logger.info("Viaje creado: {}", viajeCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(viajeCreado);
    }

    @GetMapping
    public ResponseEntity<?> listarViajes() {
        List<Viaje> viajes = viajeService.listarViajes();
        List<ViajeDTO> viajeDTOs = ViajeMapper.toDTOList(viajes);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Viajes obtenidos con éxito",
                "data", viajeDTOs
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarViaje(@PathVariable Long id, @Valid @RequestBody ViajeDTO viajeDTO) {
        Viaje viajeActualizado = viajeService.editarViaje(id, viajeDTO);
        logger.info("Viaje actualizado: {}", viajeActualizado);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Viaje actualizado con éxito",
                "data", viajeActualizado
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarViaje(@PathVariable Long id) {
        viajeService.eliminarViaje(id);
        logger.info("Viaje eliminado");
        return ResponseEntity.ok(Map.of("mensaje", "Viaje eliminado con éxito"));
    }
}
