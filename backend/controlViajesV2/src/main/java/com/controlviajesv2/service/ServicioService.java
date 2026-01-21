package com.controlviajesv2.service;

import com.controlviajesv2.dto.ServicioDTO;

import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para la entidad Servicio.
 */
public interface ServicioService {

    // Crear un nuevo servicio
    ServicioDTO crearServicio(ServicioDTO servicioDTO);

    // Listar todos los servicios
    List<ServicioDTO> listarServicios();

    // Obtener un servicio por su ID
    ServicioDTO obtenerServicioPorId(Long id);

    // Actualizar un servicio existente
    ServicioDTO actualizarServicio(Long id, ServicioDTO servicioDTO);

    // Eliminar un servicio por su ID
    void eliminarServicio(Long id);

    //busca los servicios de una empresa por su id
    List<ServicioDTO> findByEmpresaId(Long empresaId);



}