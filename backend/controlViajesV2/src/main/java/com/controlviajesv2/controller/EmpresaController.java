package com.controlviajesv2.controller;


import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.service.EmpresaService;
import com.controlviajesv2.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Empresas", description = "Operaciones para gestionar las empresas del usuario") // Título de la sección en Swagger
public class EmpresaController {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    /**
     * Devuelve la lista de todas las empresas.
     */
    @Operation(
            summary = "Listar mis empresas",
            description = "Devuelve un listado de todas las empresas que pertenecen al usuario logueado."
    )
    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        logger.info("Solicitando listado de todas las empresas");
        List<EmpresaDTO> empresas = empresaService.listarEmpresas();
        return ResponseEntity.ok(empresas);
    }

    /**
     * Devuelve una empresa por su ID.
     */
    @Operation(summary = "Obtener detalles de una empresa", description = "Busca una empresa por su ID. Solo funciona si la empresa pertenece al usuario.")
    @GetMapping(AppConstants.REQUEST_EMPRESACONTROLLER_ID)
    public ResponseEntity<EmpresaDTO> obtenerEmpresa(@PathVariable Long id) {
        logger.info("Buscando empresa con ID: {}", id);
        EmpresaDTO empresa = empresaService.obtenerEmpresaPorId(id);
        return ResponseEntity.ok(empresa);
    }


    /**
     * Crea una nueva empresa a partir de un DTO válido.
     */

    @Operation(
            summary = "Crear una nueva empresa",
            description = "Crea una empresa y la asocia automáticamente al usuario autenticado mediante el Token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (nombre vacío, email incorrecto, etc.)"),
            @ApiResponse(responseCode = "403", description = "Token inválido o expirado")
    })
    @PostMapping
    public ResponseEntity<EmpresaDTO> crearEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        logger.info("Creando empresa: {}", empresaDTO.getNombre());
        EmpresaDTO nueva = empresaService.crearEmpresa(empresaDTO);
        return ResponseEntity.ok(nueva);
    }

    /**
     * Actualiza los datos de una empresa existente.
     */
    @Operation(summary = "Actualizar empresa", description = "Actualiza los datos de una empresa existente.")
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
    @Operation(summary = "Eliminar empresa", description = "Elimina una empresa y todos sus datos asociados (viajes, facturas, etc.).")
    @DeleteMapping(AppConstants.REQUEST_EMPRESACONTROLLER_ID)
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        logger.info("Eliminando empresa con ID: {}", id);
        empresaService.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }




}
