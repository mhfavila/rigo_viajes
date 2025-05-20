package com.rigoV2.controlViajesV2.service;

import com.rigoV2.controlViajesV2.dto.UsuarioDTO;
import com.rigoV2.controlViajesV2.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioDTO> obtenerTodos();

    UsuarioDTO obtenerPorId(Long id);

    UsuarioDTO crear(UsuarioDTO usuarioDTO);

    UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO);

    void eliminar(Long id);



}
