package com.rigo.control_viajes.service;

import com.rigo.control_viajes.DTO.EmpresaDTO;
import com.rigo.control_viajes.model.Empresa;

import java.util.List;

public interface EmpresaService {
    Empresa crearEmpresa(Empresa empresa) ;
    List<Empresa> listarEmpresas();
    Empresa obtenerEmpresaConViajes(Long id);
    Empresa editarEmpresa(Long id, EmpresaDTO empresaDTO);
    void eliminarEmpresa(Long id);
}
