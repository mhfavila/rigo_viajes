package com.controlviajesv2.mapper;




import com.controlviajesv2.dto.FacturaDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FacturaMapper {


    public FacturaDTO toDTO(Factura factura) {
        if (factura == null) return null;

        return FacturaDTO.builder()
                .id(factura.getId())
                .numeroFactura(factura.getNumeroFactura())
                .fechaEmision(factura.getFechaEmision())
                .empresaId(factura.getEmpresa() != null ? factura.getEmpresa().getId() : null)
                .usuarioId(factura.getUsuario() != null ? factura.getUsuario().getId() : null)
                .totalBruto(factura.getTotalBruto())
                .porcentajeIva(factura.getPorcentajeIva())
                .importeIva(factura.getImporteIva())
                .porcentajeIrpf(factura.getPorcentajeIrpf())
                .importeIrpf(factura.getImporteIrpf())
                .totalFactura(factura.getTotalFactura())
                .cuentaBancaria(factura.getCuentaBancaria())
                .formaPago(factura.getFormaPago())
                .observaciones(factura.getObservaciones())
                .serviciosIds(factura.getServicios() != null
                        ? factura.getServicios().stream().map(Servicio::getId).collect(Collectors.toList())
                        : null)
                .estado(factura.getEstado())
                .build();
    }

    public  Factura toEntity(FacturaDTO dto, Empresa empresa, Usuario usuario) {
        if (dto == null) return null;

        return Factura.builder()
                .id(dto.getId())
                .numeroFactura(dto.getNumeroFactura())
                .fechaEmision(dto.getFechaEmision())
                .empresa(empresa)
                .usuario(usuario)
                .totalBruto(dto.getTotalBruto())
                .porcentajeIva(dto.getPorcentajeIva())
                .importeIva(dto.getImporteIva())
                .porcentajeIrpf(dto.getPorcentajeIrpf())
                .importeIrpf(dto.getImporteIrpf())
                .totalFactura(dto.getTotalFactura())
                .cuentaBancaria(dto.getCuentaBancaria())
                .formaPago(dto.getFormaPago())
                .observaciones(dto.getObservaciones())
                .estado(dto.getEstado())

                .build();
    }
}
