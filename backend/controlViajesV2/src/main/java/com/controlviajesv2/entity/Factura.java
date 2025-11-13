package com.controlviajesv2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "total_bruto", precision = 100, scale = 2, nullable = false)
    private BigDecimal totalBruto;

    @Column(name = "porcentaje_iva", precision = 5, scale = 2, nullable = false)
    private BigDecimal porcentajeIva;

    @Column(name = "importe_iva", precision = 10, scale = 2)
    private BigDecimal importeIva;

    @Column(name = "porcentaje_irpf", precision = 5, scale = 2)
    private BigDecimal porcentajeIrpf;

    @Column(name = "importe_irpf", precision = 10, scale = 2)
    private BigDecimal importeIrpf;

    @Column(name = "total_factura", precision = 100, scale = 2, nullable = false)
    private BigDecimal totalFactura;

    @Column(name = "cuenta_bancaria")
    private String cuentaBancaria;

    @Column(name = "forma_pago")
    private String formaPago;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "factura",  orphanRemoval = true)
    private List<Servicio> servicios;

    @Column(length = 20)
    private String estado; // Opcional: "BORRADOR", "ENVIADA", "COBRADA"
}


