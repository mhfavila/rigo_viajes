package com.controlviajesv2.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ViajeDTO {

    private Long id;
    @NotBlank(message = "El origen no puede estar vacío")
    private String origen;
    @NotBlank(message = "El destino es obligatorio")
    private String destino;
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fecha;
    @NotNull(message = "La distancia no puede ser nula")
    @DecimalMin(value = "0.0", inclusive = false, message = "La distancia debe ser mayor que 0")
    private BigDecimal distancia;

    @NotNull(message = "El precio por km no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio por km debe ser mayor que 0")
    private BigDecimal precioKm;
    @DecimalMin(value = "0.0", inclusive = true, message = "El importe total debe ser positivo o cero")
    private BigDecimal importeTotal;





    @NotNull(message = "La empresa no puede ser nula")
    private Long empresaId;  // Solo almacenamos el id de la empresa en el DTO

}


