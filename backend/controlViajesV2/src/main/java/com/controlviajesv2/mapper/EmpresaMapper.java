package com.controlviajesv2.mapper;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.dto.DireccionDTO;
import com.controlviajesv2.entity.Direccion;

public class EmpresaMapper {



    //
    public static EmpresaDTO toDTO(Empresa empresa) {
        if (empresa == null) return null;

        //obtenemos el id del usuario relacionado, no toda la entidad.
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setCif(empresa.getCif());
        dto.setDireccion(toDireccionDTO(empresa.getDireccion()));
        dto.setTelefono(empresa.getTelefono());
        dto.setIban(empresa.getIban());
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
                .direccion(toDireccionEntity(dto.getDireccion()))
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .iban(dto.getIban())
                .usuario(usuario) // Asignamos la entidad completa
                .build();
        //Usamos el patrón Builder que ya tienes definido con Lombok para construir la entidad de forma limpia.
    }
    // ========================================================================
    // MÉTODOS AUXILIARES PRIVADOS (Mapean la dirección campo a campo)
    // ========================================================================

    // De Entidad -> DTO
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

    // De DTO -> Entidad
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
