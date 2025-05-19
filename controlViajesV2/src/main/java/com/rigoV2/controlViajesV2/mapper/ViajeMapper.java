package com.rigoV2.controlViajesV2.mapper;

import com.rigoV2.controlViajesV2.dto.ViajeDTO;
import com.rigoV2.controlViajesV2.entity.Empresa;
import com.rigoV2.controlViajesV2.entity.Viaje;

public class ViajeMapper {


    //Convierte una entidad a un DTO
    public static ViajeDTO toDTO(Viaje viaje) {
        if (viaje == null) return null;

        ViajeDTO dto = new ViajeDTO();
        dto.setId(viaje.getId());
        dto.setFecha(viaje.getFecha());
        dto.setPrecioKm(viaje.getPrecioKm());
        dto.setDestino(viaje.getDestino());
        dto.setEmpresaId(viaje.getEmpresa().getId()); // Solo el ID, no la entidad completa

        return dto;
    }
//Convierte el DTO recibido del cliente en una entidad Viaje.
    public static Viaje toEntity(ViajeDTO dto, Empresa empresa) {
        if (dto == null || empresa == null) return null;

        return Viaje.builder()
                .id(dto.getId())
                .fecha(dto.getFecha())
                .precioKm(dto.getPrecioKm())
                .destino(dto.getDestino())
                .empresa(empresa) // Se le pasa la entidad completa
                .build();
    }



}
