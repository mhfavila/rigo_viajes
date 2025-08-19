package com.controlviajesv2.service;

import com.controlviajesv2.dto.ViajeDTO;
import com.controlviajesv2.entity.Viaje;

import java.util.List;

public interface ViajeService {

    List<ViajeDTO> obtenerTodos();

    ViajeDTO obtenerPorId(Long id);

    ViajeDTO crear(ViajeDTO viajeDTO);

    ViajeDTO actualizar(Long id, ViajeDTO viajeDTO);

    void eliminar(Long id);

    List<ViajeDTO> findByEmpresaId(Long empresaId);
}
