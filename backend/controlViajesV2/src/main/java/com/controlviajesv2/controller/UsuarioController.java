package com.controlviajesv2.controller;


import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.dto.UsuarioDTO;
import com.controlviajesv2.service.UsuarioService;
import com.controlviajesv2.util.AppConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST para la gestión de usuarios.
 * Proporciona endpoints CRUD para la entidad Usuario.
 */
@RestController
@RequestMapping(AppConstants.REQUEST_USUARIOCONTROLLER)
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios registrados.
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        logger.info("Listando todos los usuarios");
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por su ID.
     */
    @GetMapping(AppConstants.REQUEST_USUARIOCONTROLLER_ID)
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        logger.info("Obteniendo usuario con ID: {}", id);
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Crea un nuevo usuario.
     */

   /*
   @PostMapping

    public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("Creando nuevo usuario: {}", usuarioDTO.getNombre());
        UsuarioDTO creado = usuarioService.crear(usuarioDTO);
        return ResponseEntity.ok(creado);
    }
    */


    /**
     * Actualiza un usuario existente.
     */
    @PutMapping(AppConstants.REQUEST_USUARIOCONTROLLER_ID)
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("Actualizando usuario con ID: {}", id);
        UsuarioDTO actualizado = usuarioService.actualizar(id, usuarioDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un usuario por su ID.
     */
    @DeleteMapping(AppConstants.REQUEST_USUARIOCONTROLLER_ID)
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        logger.info("Eliminando usuario con ID: {}", id);
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }



/**
Sacar las empresas de un usuario
 */
    @GetMapping(AppConstants.REQUEST_USUARIOCONTROLLER_empresas)
    public ResponseEntity<List<EmpresaDTO>> obtenerEmpresasDeUsuario(@PathVariable Long id) {
        List<EmpresaDTO> empresas = usuarioService.obtenerEmpresasDeUsuario(id);
        return ResponseEntity.ok(empresas);
    }





}
