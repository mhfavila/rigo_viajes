package com.rigo.control_viajes.mapper;

import com.rigo.control_viajes.DTO.EmpresaDTO;
import com.rigo.control_viajes.model.Empresa;

import java.util.List;

public class EmpresaMapper {
    // Convierte una entidad Empresa a un DTO
    public static EmpresaDTO toDTO(Empresa empresa) {
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setId(empresa.getId());
        empresaDTO.setNombre(empresa.getNombre());
        empresaDTO.setCif(empresa.getCif());
        empresaDTO.setTelefono(empresa.getTelefono());
        empresaDTO.setEmail(empresa.getEmail());
        return empresaDTO;
    }

    // (opcional) Convierte una lista de entidades Empresa a una lista de DTOs
    public static List<EmpresaDTO> toDTOList(List<Empresa> empresas) {
        return empresas.stream()
                .map(EmpresaMapper::toDTO)
                .toList();
    }
}
