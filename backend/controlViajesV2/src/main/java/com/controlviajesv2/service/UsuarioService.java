package com.controlviajesv2.service;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> obtenerTodos();

    UsuarioDTO obtenerPorId(Long id);

    //el usuario se crea en el register del authcontroller
    //UsuarioDTO crear(UsuarioDTO usuarioDTO);

    UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO);

    void eliminar(Long id);

    List<EmpresaDTO> obtenerEmpresasDeUsuario(Long usuarioId);



}
