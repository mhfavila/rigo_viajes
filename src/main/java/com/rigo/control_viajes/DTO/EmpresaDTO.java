package com.rigo.control_viajes.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmpresaDTO {
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El CIF es obligatorio")
    private String cif;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    // Constructor con parámetros

    public EmpresaDTO( String nombre, String cif, String telefono, String email) {

        this.nombre = nombre;
        this.cif = cif;
        this.telefono = telefono;
        this.email = email;
    }

    public EmpresaDTO() {
    }



    // Getters y Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmpresaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cif='" + cif + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
