package com.controlviajesv2.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    // --- NUEVOS CAMPOS FISCALES ---
    @Column(length = 24)
    private String cuentaBancaria; // DNI o CIF del autónomo/empresa emisora

    @Column(length = 9)
    private String nif; // DNI o CIF del autónomo/empresa emisora

    @Column(length = 9)
    private String telefono;

    // Reutilizamos tu clase Direccion para tener calle, cp, ciudad, etc.
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

    @Column(name = "imagen_url")
    private String imagenUrl; // Para guardar la URL de tu logo si lo subes a la nube


    //esto servira para crear una tabla usuario_roles donde un usauario podra tener varios roles
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<String> roles;
}
