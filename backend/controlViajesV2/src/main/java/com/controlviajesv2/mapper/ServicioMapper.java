package com.controlviajesv2.mapper;

import com.controlviajesv2.dto.DireccionDTO;
import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.entity.Direccion;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;

public class ServicioMapper {

    public static ServicioDTO toDTO(Servicio servicio) {
        if (servicio == null) return null;

        return ServicioDTO.builder()
                .id(servicio.getId())
                .facturaId(servicio.getFactura() != null ? servicio.getFactura().getId() : null)
                .empresaId(servicio.getEmpresa().getId())
                .tipoServicio(servicio.getTipoServicio())
                .fechaServicio(servicio.getFechaServicio())
                .origen(toDireccionDTO(servicio.getOrigen()))
                .destino(toDireccionDTO(servicio.getDestino()))
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

    public static Servicio toEntity(ServicioDTO dto, Empresa empresa) {
        if (dto == null) return null;

        return Servicio.builder()
                .id(dto.getId())

                .empresa(empresa)
                .tipoServicio(dto.getTipoServicio())
                .fechaServicio(dto.getFechaServicio())
                .origen(toDireccionEntity(dto.getOrigen()))
                .destino(toDireccionEntity(dto.getDestino()))
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



    // ========================================================================
    // MÉTODOS AUXILIARES (Para reutilizar la conversión)
    // ========================================================================

    private static DireccionDTO toDireccionDTO(Direccion direccion) {
        if (direccion == null) return null;

        return DireccionDTO.builder()
                .calle(direccion.getCalle())
                .numero(direccion.getNumero())
                .codigoPostal(direccion.getCodigoPostal())
                .ciudad(direccion.getCiudad())
                .provincia(direccion.getProvincia())
                .pais(direccion.getPais())
                .build();
    }

    private static Direccion toDireccionEntity(DireccionDTO dto) {
        if (dto == null) return null;

        return Direccion.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .codigoPostal(dto.getCodigoPostal())
                .ciudad(dto.getCiudad())
                .provincia(dto.getProvincia())
                .pais(dto.getPais())
                .build();
    }
}
