package com.controlviajesv2.mapper;

import com.controlviajesv2.dto.ViajeDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Viaje;

public class ViajeMapper {


    //Convierte una entidad a un DTO
    public static ViajeDTO toDTO(Viaje viaje) {
        if (viaje == null) return null;

        return ViajeDTO.builder()
                .id(viaje.getId())
                .origen(viaje.getOrigen())
                .destino(viaje.getDestino())
                .fecha(viaje.getFecha())
                .distancia(viaje.getDistancia())
                .precioKm(viaje.getPrecioKm())
                .importeTotal(viaje.getImporteTotal())
                .empresaId(viaje.getEmpresa() != null ? viaje.getEmpresa().getId() : null)
                .build();
    }
//Convierte el DTO recibido del cliente en una entidad Viaje.
    public static Viaje toEntity(ViajeDTO dto, Empresa empresa) {
        if (dto == null || empresa == null) return null;

        return Viaje.builder()
                .id(dto.getId())
                .origen(dto.getOrigen())
                .destino(dto.getDestino())
                .fecha(dto.getFecha())
                .distancia(dto.getDistancia())
                .precioKm(dto.getPrecioKm())
                .importeTotal(dto.getImporteTotal())
                .empresa(empresa)
                .build();
    }



}
