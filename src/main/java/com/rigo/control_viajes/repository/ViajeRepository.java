package com.rigo.control_viajes.repository;

import com.rigo.control_viajes.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
// Interfaz para acceder a los datos de la tabla viaje
public interface ViajeRepository extends JpaRepository<Viaje, Long> {}
// Spring Data JPA proporciona métodos CRUD básicos para la tabla viaje
