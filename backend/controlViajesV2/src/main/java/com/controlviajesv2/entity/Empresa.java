package com.controlviajesv2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "empresas")
public class Empresa {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String cif;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "direccion_calle")),
            @AttributeOverride(name = "numero", column = @Column(name = "direccion_numero")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "direccion_cp")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "direccion_ciudad")),
            @AttributeOverride(name = "provincia", column = @Column(name = "direccion_provincia")),
            @AttributeOverride(name = "pais", column = @Column(name = "direccion_pais"))
    })
    private Direccion direccion;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "iban", length = 34)
    private String iban; // IBAN de la empresa emisora

    // --- NUEVOS CAMPOS PARA PRECIOS POR DEFECTO ---
    @Column(name = "precio_km_defecto", precision = 10, scale = 2)
    private BigDecimal precioKmDefecto;

    @Column(name = "precio_hora_espera_defecto", precision = 10, scale = 2)
    private BigDecimal precioHoraEsperaDefecto;

    @Column(name = "precio_dieta_defecto", precision = 10, scale = 2)
    private BigDecimal precioDietaDefecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Viaje> viajes;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas;
}
