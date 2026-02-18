package com.controlviajesv2.mapper;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.dto.DireccionDTO;
import com.controlviajesv2.entity.Direccion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmpresaMapper {


    public EmpresaDTO toDTO(Empresa empresa) {
        if (empresa == null) return null;

        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setCif(empresa.getCif());
        dto.setDireccion(toDireccionDTO(empresa.getDireccion()));
        dto.setTelefono(empresa.getTelefono());
        dto.setIban(empresa.getIban());
        dto.setEmail(empresa.getEmail());

        // Manejo seguro del usuario (evitar NullPointerException)
        if (empresa.getUsuario() != null) {
            dto.setUsuarioId(empresa.getUsuario().getId());
        }

        dto.setPrecioKmDefecto(empresa.getPrecioKmDefecto());
        dto.setPrecioHoraEsperaDefecto(empresa.getPrecioHoraEsperaDefecto());
        dto.setPrecioDietaDefecto(empresa.getPrecioDietaDefecto());

        return dto;
    }

    public Empresa toEntity(EmpresaDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) return null;

        return Empresa.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .cif(dto.getCif())
                .direccion(toDireccionEntity(dto.getDireccion()))
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .iban(dto.getIban())
                .precioDietaDefecto(dto.getPrecioDietaDefecto())
                .precioHoraEsperaDefecto(dto.getPrecioHoraEsperaDefecto())
                .precioKmDefecto(dto.getPrecioKmDefecto())
                .usuario(usuario)
                .build();
    }

    // 👇 3. EL MÉTODO NUEVO QUE FALTABA (Para convertir listas)
    public List<EmpresaDTO> toDtoList(List<Empresa> empresas) {
        if (empresas == null) return null;
        return empresas.stream()
                .map(this::toDTO) // Llama al método toDTO uno por uno
                .collect(Collectors.toList());
    }



    private DireccionDTO toDireccionDTO(Direccion direccion) {
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

    private Direccion toDireccionEntity(DireccionDTO dto) {
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