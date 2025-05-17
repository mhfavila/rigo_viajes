package com.rigo.control_viajes.mapper;

import com.rigo.control_viajes.DTO.ViajeDTO;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.model.Viaje;

import java.util.List;
import java.util.stream.Collectors;

public class ViajeMapper {
    public static Viaje toEntity(ViajeDTO dto, Empresa empresa) {
        Viaje viaje = new Viaje();
        viaje.setFecha(dto.getFecha());
        viaje.setDistancia(dto.getDistancia());
        viaje.setPrecioKm(dto.getPrecioKm());
        viaje.setEmpresa(empresa);
        return viaje;
    }

    // Método para convertir una lista de Viaje a ViajeDTO
    public static List<ViajeDTO> toDTOList(List<Viaje> viajes) {
        return viajes.stream()
                .map(viaje -> {
                    ViajeDTO viajeDTO = new ViajeDTO();
                    viajeDTO.setId(viaje.getId());
                    viajeDTO.setFecha(viaje.getFecha());
                    viajeDTO.setDistancia(viaje.getDistancia());
                    viajeDTO.setPrecioKm(viaje.getPrecioKm());
                    viajeDTO.setEmpresaId(viaje.getEmpresa().getId());
                    return viajeDTO;
                })
                .collect(Collectors.toList());
    }
}
