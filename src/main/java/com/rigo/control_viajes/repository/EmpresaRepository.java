package com.rigo.control_viajes.repository;

import com.rigo.control_viajes.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
// Interfaz para acceder a los datos de la tabla empresa
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {}

// Spring Data JPA ya proporciona todos los métodos CRUD básicos
