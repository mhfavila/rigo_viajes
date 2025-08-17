package com.controlviajesv2.controller;


import com.controlviajesv2.dto.EmpresaConViajesDTO;
import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.service.EmpresaService;
import com.controlviajesv2.util.AppConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar empresas.
 * Expone endpoints para operaciones CRUD.
 */
@RestController
@RequestMapping(AppConstants.REQUEST_EMPRESACONTROLLER)
public class EmpresaController {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    /**
     * Devuelve la lista de todas las empresas.
     */
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        logger.info("Solicitando listado de todas las empresas");
        List<EmpresaDTO> empresas = empresaService.listarEmpresas();
        return ResponseEntity.ok(empresas);
    }

    /**
     * Devuelve una empresa por su ID.
     */
    @GetMapping(AppConstants.REQUEST_EMPRESACONTROLLER_ID)
    public ResponseEntity<EmpresaDTO> obtenerEmpresa(@PathVariable Long id) {
        logger.info("Buscando empresa con ID: {}", id);
        EmpresaDTO empresa = empresaService.obtenerEmpresaPorId(id);
        return ResponseEntity.ok(empresa);
    }


    /**
     * Crea una nueva empresa a partir de un DTO válido.
     */
    @PostMapping
    public ResponseEntity<EmpresaDTO> crearEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        logger.info("Creando empresa: {}", empresaDTO.getNombre());
        EmpresaDTO nueva = empresaService.crearEmpresa(empresaDTO);
        return ResponseEntity.ok(nueva);
    }

    /**
     * Actualiza los datos de una empresa existente.
     */
    @PutMapping(AppConstants.REQUEST_EMPRESACONTROLLER_ID)
    public ResponseEntity<EmpresaDTO> actualizarEmpresa(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaDTO empresaDTO) {
        logger.info("Actualizando empresa con ID: {}", id);
        EmpresaDTO actualizada = empresaService.actualizarEmpresa(id, empresaDTO);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina una empresa por su ID.
     */
    @DeleteMapping(AppConstants.REQUEST_EMPRESACONTROLLER_ID)
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        logger.info("Eliminando empresa con ID: {}", id);
        empresaService.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Devuelve una lista de empresas con sus viajes asociados.
     */
    @GetMapping(AppConstants.REQUEST_EMPRESACONTROLLER_CONVIAJES)
    public ResponseEntity<List<EmpresaConViajesDTO>> listarEmpresas_Viajes() {
        logger.info("Solicitando listado de todas las empresas");
        List<EmpresaConViajesDTO> empresas = empresaService.listarEmpresas_Viajes();
        return ResponseEntity.ok(empresas);
    }


}
