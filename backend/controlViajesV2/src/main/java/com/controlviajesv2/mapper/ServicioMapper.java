package com.controlviajesv2.mapper;

import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;

public class ServicioMapper {

    public static ServicioDTO toDTO(Servicio servicio) {
        if (servicio == null) return null;

        return ServicioDTO.builder()
                .id(servicio.getId())
                .facturaId(servicio.getFactura() != null ? servicio.getFactura().getId() : null)
                .tipoServicio(servicio.getTipoServicio())
                .fechaServicio(servicio.getFechaServicio())
                .origen(servicio.getOrigen())
                .destino(servicio.getDestino())
                .conductor(servicio.getConductor())
                .matriculaVehiculo(servicio.getMatriculaVehiculo())
                .km(servicio.getKm())
                .precioKm(servicio.getPrecioKm())
                .importeServicio(servicio.getImporteServicio())
                .dieta(servicio.getDieta())
                .precioDieta(servicio.getPrecioDieta())
                .horasEspera(servicio.getHorasEspera())
                .importeEspera(servicio.getImporteEspera())
                .albaran(servicio.getAlbaran())
                .clienteFinal(servicio.getClienteFinal())
                .observaciones(servicio.getObservaciones())
                .orden(servicio.getOrden())
                .build();
    }

    public static Servicio toEntity(ServicioDTO dto, Factura factura) {
        if (dto == null) return null;

        return Servicio.builder()
                .id(dto.getId())
                .factura(factura)
                .tipoServicio(dto.getTipoServicio())
                .fechaServicio(dto.getFechaServicio())
                .origen(dto.getOrigen())
                .destino(dto.getDestino())
                .conductor(dto.getConductor())
                .matriculaVehiculo(dto.getMatriculaVehiculo())
                .km(dto.getKm())
                .precioKm(dto.getPrecioKm())
                .importeServicio(dto.getImporteServicio())
                .dieta(dto.getDieta())
                .precioDieta(dto.getPrecioDieta())
                .horasEspera(dto.getHorasEspera())
                .importeEspera(dto.getImporteEspera())
                .albaran(dto.getAlbaran())
                .clienteFinal(dto.getClienteFinal())
                .observaciones(dto.getObservaciones())
                .orden(dto.getOrden())
                .build();
    }
}
