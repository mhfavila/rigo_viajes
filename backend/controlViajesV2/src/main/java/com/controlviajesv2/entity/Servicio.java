package com.controlviajesv2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;







@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id")
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "tipo_servicio", nullable = false)
    private String tipoServicio;

    @Column(name = "fecha_servicio", nullable = false)
    private LocalDate fechaServicio;

    // --- ORIGEN ---
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "origen_calle")),
            @AttributeOverride(name = "numero", column = @Column(name = "origen_numero")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "origen_cp")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "origen_ciudad")),
            @AttributeOverride(name = "provincia", column = @Column(name = "origen_provincia")),
            @AttributeOverride(name = "pais", column = @Column(name = "origen_pais"))
    })
    private Direccion origen;

    // --- DESTINO ---
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "destino_calle")),
            @AttributeOverride(name = "numero", column = @Column(name = "destino_numero")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "destino_cp")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "destino_ciudad")),
            @AttributeOverride(name = "provincia", column = @Column(name = "destino_provincia")),
            @AttributeOverride(name = "pais", column = @Column(name = "destino_pais"))
    })
    private Direccion destino;

    @Column
    private String conductor;

    @Column(name = "matricula_vehiculo")
    private String matriculaVehiculo;

    @Column(nullable = false)
    private Integer km;

    @Column(name = "precio_km", precision = 100, scale = 2, nullable = false)
    private BigDecimal precioKm;

    @Column(name = "importe_servicio", precision = 100, scale = 2, nullable = false)
    private BigDecimal importeServicio;

    @Column(name = "dieta")
    private Boolean dieta;

    @Column(name = "precio_dieta", precision = 100, scale = 2)
    private BigDecimal precioDieta;

    @Column(name = "horas_espera", precision = 100, scale = 2)
    private BigDecimal horasEspera;

    @Column(name = "importe_espera", precision = 100, scale = 2)
    private BigDecimal importeEspera;

    @Column
    private String albaran;

    @Column(name = "cliente_final")
    private String clienteFinal;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column
    private Integer orden; // opcional
}