package com.controlviajesv2.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "viajes")

public class Viaje {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal distancia;

    @Column(name = "precio_km", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioKm;

    @Column(name = "importe_total", precision = 10, scale = 2)
    private BigDecimal importeTotal;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;



    // Si algún día relacionas viajes con facturas
   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id")
    private Factura factura;*/
}
