package com.controlviajesv2.mapper;

import com.controlviajesv2.dto.DireccionDTO;
import com.controlviajesv2.dto.UsuarioDTO;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.entity.Direccion;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {


    /**
     * Convierte una entidad Usuario en un UsuarioDTO.
     */
    public  UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        //dto.setPassword(usuario.getPassword());
        dto.setEmail(usuario.getEmail());
        dto.setRoles(usuario.getRoles());

        // --- NUEVOS CAMPOS ---
        dto.setNif(usuario.getNif());
        dto.setTelefono(usuario.getTelefono());
        dto.setImagenUrl(usuario.getImagenUrl());
        dto.setCuentaBancaria(usuario.getCuentaBancaria());

        // Usamos el método auxiliar para la dirección
        dto.setDireccion(toDireccionDTO(usuario.getDireccion()));

        return dto;
    }

    /**
     * Convierte un UsuarioDTO en una entidad Usuario.
     */
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setPassword(dto.getPassword());
        usuario.setEmail(dto.getEmail());
        usuario.setRoles(dto.getRoles());

        // --- NUEVOS CAMPOS ---
        usuario.setNif(dto.getNif());
        usuario.setTelefono(dto.getTelefono());
        usuario.setImagenUrl(dto.getImagenUrl());
        usuario.setCuentaBancaria(dto.getCuentaBancaria());

        // Usamos el método auxiliar para la dirección
        usuario.setDireccion(toDireccionEntity(dto.getDireccion()));

        return usuario;
    }

    // ========================================================================
    // MÉTODOS AUXILIARES PRIVADOS (Copiados para mapear dirección en Usuario)
    // ========================================================================

    // De Entidad -> DTO
    private  DireccionDTO toDireccionDTO(Direccion direccion) {
        if (direccion == null) return null;

        // Aquí usamos el Builder de DireccionDTO porque ese objeto sí tiene @Builder
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
    private  Direccion toDireccionEntity(DireccionDTO dto) {
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