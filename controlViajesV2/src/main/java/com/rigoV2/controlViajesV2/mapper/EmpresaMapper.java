package com.rigoV2.controlViajesV2.mapper;

import com.rigoV2.controlViajesV2.dto.EmpresaDTO;
import com.rigoV2.controlViajesV2.entity.Empresa;
import com.rigoV2.controlViajesV2.entity.Usuario;

public class EmpresaMapper {



    //
    public static EmpresaDTO toDTO(Empresa empresa) {
        if (empresa == null) return null;

        //obtenemos el id del usuario relacionado, no toda la entidad.
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setCif(empresa.getCif());
        dto.setDireccion(empresa.getDireccion());
        dto.setTelefono(empresa.getTelefono());
        dto.setEmail(empresa.getEmail());
        dto.setUsuarioId(empresa.getUsuario().getId());
        //Solo se incluye el id del usuario (usuario.getId()), no toda la entidad Usuario

        return dto;
    }

    public static Empresa toEntity(EmpresaDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) return null;

        return Empresa.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .cif(dto.getCif())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .usuario(usuario) // Asignamos la entidad completa
                .build();
        //Usamos el patrón Builder que ya tienes definido con Lombok para construir la entidad de forma limpia.
    }


}
