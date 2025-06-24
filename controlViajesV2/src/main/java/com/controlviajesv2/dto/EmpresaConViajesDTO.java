package com.controlviajesv2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmpresaConViajesDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private List<ViajeDTO> viajes;
}
