package com.controlviajesv2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ViajeDTO {

    private Long id;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El precio es obligatorio")
    private Double precioKm;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    @NotNull(message = "La distancia es obligatoria")
    private Double distancia;

    @NotNull(message = "El ID de la empresa es obligatorio")
    private Long empresaId;
}
