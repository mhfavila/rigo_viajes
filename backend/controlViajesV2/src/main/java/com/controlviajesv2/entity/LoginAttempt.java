package com.controlviajesv2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clave; // Puede ser la IP o el Username

    @Column(nullable = false)
    private int intentos;

    @Column(nullable = false)
    private LocalDateTime ultimaModificacion;

    public LoginAttempt(String clave,int intentos,LocalDateTime ultimaModificacion) {
        this.clave = clave;
        this.intentos = intentos;
        this.ultimaModificacion = ultimaModificacion;
    }




}