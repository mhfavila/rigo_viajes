package com.controlviajesv2.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacturaDTO {

    private Long id;

    @NotBlank(message = "El número de factura no puede estar vacío")
    private String numeroFactura;

    @NotNull(message = "La fecha de emisión no puede ser nula")
    private LocalDate fechaEmision;

    @NotNull(message = "La empresa no puede ser nula")
    private Long empresaId;

    @NotNull(message = "El usuario no puede ser nulo")
    private Long usuarioId;

    @NotNull(message = "El total bruto no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total bruto debe ser mayor que 0")
    private BigDecimal totalBruto;

    @NotNull(message = "El porcentaje de IVA no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El porcentaje de IVA no puede ser negativo")
    private BigDecimal porcentajeIva;

    @DecimalMin(value = "0.0", inclusive = true, message = "El importe IVA no puede ser negativo")
    private BigDecimal importeIva;

    @DecimalMin(value = "0.0", inclusive = true, message = "El porcentaje IRPF no puede ser negativo")
    private BigDecimal porcentajeIrpf;

    @DecimalMin(value = "0.0", inclusive = true, message = "El importe IRPF no puede ser negativo")
    private BigDecimal importeIrpf;

    @NotNull(message = "El total de la factura no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total de la factura debe ser mayor que 0")
    private BigDecimal totalFactura;

    private String cuentaBancaria;

    private String formaPago;

    private String observaciones;

    private List<Long> serviciosIds; // Opcional: solo IDs de servicios

    @Pattern(regexp = "BORRADOR|ENVIADA|COBRADA", message = "El estado debe ser BORRADOR, ENVIADA o COBRADA")
    private String estado;
}
