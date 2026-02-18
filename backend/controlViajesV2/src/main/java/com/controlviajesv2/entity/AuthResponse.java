package com.controlviajesv2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // <-- Esta es la que te falta para aceptar el token en el constructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
}
