package com.controlviajesv2.entity;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class Direccion {
    private String calle;
    private String numero;
    private String ciudad;
    private String codigoPostal;
    private String provincia;
    private String pais;


}
