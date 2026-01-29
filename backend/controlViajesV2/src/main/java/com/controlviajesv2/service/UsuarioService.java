package com.controlviajesv2.service;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    // Método nuevo: Obtiene el usuario que está logueado
    UsuarioDTO obtenerUsuarioActual();

    // Método nuevo: Actualiza el usuario que está logueado
    UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO);

    // Mantenemos este por si acaso (para uso interno)
    UsuarioDTO obtenerPorId(Long id);


}