package com.rigo.control_viajes.service;

import com.rigo.control_viajes.DTO.EmpresaDTO;
import com.rigo.control_viajes.DTO.ViajeDTO;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.model.Viaje;

import java.util.List;

public interface ViajeService {
    Viaje crearViaje(ViajeDTO viajeDTo);
    List<Viaje> listarViajes();
    Viaje editarViaje(Long id, ViajeDTO viajeDTO);
    void eliminarViaje(Long id);
}
