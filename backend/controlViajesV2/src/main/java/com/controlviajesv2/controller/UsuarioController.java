package com.controlviajesv2.controller;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.dto.UsuarioDTO;
import com.controlviajesv2.service.UsuarioService;
import com.controlviajesv2.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstants.REQUEST_USUARIOCONTROLLER)
@Tag(name = "Perfil de Usuario", description = "Gestión de la cuenta del usuario logueado")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // YA NO USAMOS ID EN LA URL (Más seguro)

    @Operation(summary = "Ver mi perfil", description = "Obtiene los datos del usuario actualmente autenticado via Token.")
    @GetMapping(AppConstants.REQUEST_USUARIOCONTROLLER_PERFIL)
    public ResponseEntity<UsuarioDTO> obtenerPerfil() {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioActual();
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Actualizar mi perfil", description = "Permite modificar tus propios datos personales.")
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioDTO> actualizarPerfil(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO actualizado = usuarioService.actualizarUsuario(usuarioDTO);
        return ResponseEntity.ok(actualizado);
    }

}