package com.controlviajesv2.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaDTO {


    private Long id;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El CIF es obligatorio")
    @Pattern(
            regexp = "^[A-Z0-9]{8,10}$",
            message = "El CIF debe tener un formato válido (letras y números, entre 8 y 10 caracteres)"
    )
    private String cif;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^[0-9+\\- ]{9,20}$",
            message = "El teléfono solo puede contener números"
    )
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @Pattern(
            regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$",
            message = "El IBAN debe tener un formato válido (por ejemplo, ES76...)"
    )
    private String iban;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
}
