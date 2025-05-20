package com.rigoV2.controlViajesV2.service;

import com.rigoV2.controlViajesV2.dto.ViajeDTO;

import java.util.List;

public interface ViajeService {

    List<ViajeDTO> obtenerTodos();

    ViajeDTO obtenerPorId(Long id);

    ViajeDTO crear(ViajeDTO viajeDTO);

    ViajeDTO actualizar(Long id, ViajeDTO viajeDTO);

    void eliminar(Long id);
}
