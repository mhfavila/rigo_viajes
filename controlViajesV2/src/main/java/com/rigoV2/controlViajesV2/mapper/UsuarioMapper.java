package com.rigoV2.controlViajesV2.mapper;

import com.rigoV2.controlViajesV2.dto.UsuarioDTO;
import com.rigoV2.controlViajesV2.entity.Usuario;

public class UsuarioMapper {


    /**
     * Convierte una entidad Usuario en un UsuarioDTO.
     * Esto se hace cuando queremos enviar datos al cliente (frontend)
     * @param usuario
     * @return
     */
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());


        return dto;
    }

    /**
     *  Convierte un UsuarioDTO en una entidad Usuario.
     *  Esto se usa cuando el cliente envía datos (por ejemplo, en un formulario).
     * @param dto
     * @return
     */
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());


        return usuario;
    }
}
