package com.rigo.control_viajes.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rigo.control_viajes.model.Empresa;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public class ViajeDTO {

    private Long id;

    @NotNull(message = "La Fecha es obligatorio")
    private LocalDate fecha;
    @NotNull(message = "La distancia es obligatoria")
    @PositiveOrZero(message = "La distancia debe ser mayor que cero o cero")
    private Double distancia;

    @NotNull(message = "La distancia es obligatoria")
    @PositiveOrZero(message = "La distancia debe ser mayor que cero o cero")
    private Double precioKm;

    @NotNull(message = "El id de la empresa es obligatoria")
    private Long empresaId;

    public ViajeDTO(LocalDate fecha, Double distancia, Double precioKm, Long empresaId) {

        this.fecha = fecha;
        this.distancia = distancia;
        this.precioKm = precioKm;
        this.empresaId = empresaId;
    }

    public ViajeDTO() {
    }

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


    public Double getPrecioKm() {
        return precioKm;
    }

    public void setPrecioKm(Double precioKm) {
        this.precioKm = precioKm;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    @Override
    public String toString() {
        return "ViajeDTO{" +
                "fecha=" + fecha +
                ", precioKm=" + precioKm +
                ", distancia=" + distancia +
                ", empresaId=" + empresaId +
                '}';
    }

}
