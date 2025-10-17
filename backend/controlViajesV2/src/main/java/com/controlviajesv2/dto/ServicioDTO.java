package com.controlviajesv2.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicioDTO {

    private Long id;

    @NotNull(message = "La factura no puede ser nula")
    private Long facturaId;

    @NotBlank(message = "El tipo de servicio no puede estar vacío")
    private String tipoServicio;

    @NotNull(message = "La fecha del servicio no puede ser nula")
    private LocalDate fechaServicio;

    @NotBlank(message = "El origen no puede estar vacío")
    private String origen;

    @NotBlank(message = "El destino no puede estar vacío")
    private String destino;

    private String conductor;

    private String matriculaVehiculo;

    @NotNull(message = "Los kilómetros no pueden ser nulos")
    @Min(value = 0, message = "Los kilómetros no pueden ser negativos")
    private Integer km;

    @NotNull(message = "El precio por km no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio por km debe ser mayor que 0")
    private BigDecimal precioKm;

    @NotNull(message = "El importe del servicio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El importe del servicio debe ser mayor que 0")
    private BigDecimal importeServicio;

    @DecimalMin(value = "0.0", inclusive = true, message = "La dieta no puede ser negativa")
    private BigDecimal dieta;

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio de dieta no puede ser negativo")
    private BigDecimal precioDieta;

    @DecimalMin(value = "0.0", inclusive = true, message = "Las horas de espera no pueden ser negativas")
    private BigDecimal horasEspera;

    @DecimalMin(value = "0.0", inclusive = true, message = "El importe de espera no puede ser negativo")
    private BigDecimal importeEspera;

    private String albaran;

    private String clienteFinal;

    private String observaciones;

    @Min(value = 0, message = "El orden no puede ser negativo")
    private Integer orden;
}
