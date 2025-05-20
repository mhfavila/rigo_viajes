package com.rigoV2.controlViajesV2.service;

import com.rigoV2.controlViajesV2.dto.EmpresaDTO;


import java.util.List;


public interface EmpresaService {


    // Crea una nueva empresa en el sistema
    EmpresaDTO crearEmpresa(EmpresaDTO empresaDTO);

    // Devuelve todas las empresas registradas
    List<EmpresaDTO> listarEmpresas();

    // Busca una empresa por su ID
    EmpresaDTO obtenerEmpresaPorId(Long id);

    // Actualiza los datos de una empresa existente
    EmpresaDTO actualizarEmpresa(Long id, EmpresaDTO empresaDTO);

    // Elimina una empresa por su ID
    void eliminarEmpresa(Long id);

}
