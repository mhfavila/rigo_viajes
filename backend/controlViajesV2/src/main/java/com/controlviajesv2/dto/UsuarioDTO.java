package com.controlviajesv2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid; // <--- IMPORTANTE
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    private String nombre;

    //@NotBlank(message = "La contraseña es obligatoria")
    //@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    private Set<String> roles;

    private String cuentaBancaria;

    @Size(max = 20, message = "El NIF no puede superar los 20 caracteres")
    private String nif;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;


    @Valid
    private DireccionDTO direccion;

    private String imagenUrl;

}
