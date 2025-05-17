package com.rigo.control_viajes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;// Importa las anotaciones JPA
import java.util.List;

@Entity // Anotación que marca esta clase como una entidad JPA
public class Empresa {
    @Id// Marca este campo como la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática del ID
    private Long id;

    private String nombre;
    private String cif;
    private String telefono;
    private String email;

    // Relación uno a muchos con la clase Viaje
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Viaje> viajes;// Lista de viajes asociados a esta empresa

    //contructor
    public Empresa() {

    }

    public Empresa(Long id, String nombre, String cif, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.cif = cif;
        this.telefono = telefono;
        this.email = email;

    }



    // Getters y Setters para acceder a las propiedades de la clase
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

    public List<Viaje> getViajes() {
        return viajes;
    }

    public void setViajes(List<Viaje> viajes) {
        this.viajes = viajes;
    }


    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cif='" + cif + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
