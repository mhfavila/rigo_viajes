package com.rigo.control_viajes.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private Double distancia;
    private Double precioKm;

    @ManyToOne
    @JoinColumn(name = "empresa_id",nullable = false)
    //@JsonIgnore // Evita la serialización del campo "empresa" para evitar el ciclo
    @JsonBackReference//por que el JsonIgnore no me dejaba crear los viajes por que no serializaba y no le llegaba el id de la empresa
    private Empresa empresa;


    //contructor
    public Viaje() {
    }

    // Getters y Setters para acceder a las propiedades de la clase
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Double getPrecioKm() {
        return precioKm;
    }

    public void setPrecioKm(Double precioKm) {
        this.precioKm = precioKm;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    @Override
    public String toString() {
        return "Viaje{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", precioKm=" + precioKm +
                ", distancia=" + distancia +
                ", empresa=" + (empresa != null ? empresa.getId() + " - " + empresa.getNombre() : "null") +
                '}';
    }
}
