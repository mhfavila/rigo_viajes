package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.ViajeDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Viaje;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.ViajeMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.ViajeRepository;
import com.controlviajesv2.service.ViajeService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Viaje.
 * Encapsula la lógica de negocio para manipular viajes.
 */
@Service
@Transactional
public class ViajeServiceImpl implements ViajeService {

    private static final Logger logger = LoggerFactory.getLogger(ViajeServiceImpl.class);

    private final ViajeRepository viajeRepository;
    private final EmpresaRepository empresaRepository;

    public ViajeServiceImpl(ViajeRepository viajeRepository, EmpresaRepository empresaRepository) {
        this.viajeRepository = viajeRepository;
        this.empresaRepository = empresaRepository;
    }

    /**
     * Devuelve todos los viajes existentes en la base de datos.
     */
    @Override
    public List<ViajeDTO> obtenerTodos() {
        logger.info("Obteniendo todos los viajes");
        return viajeRepository.findAll()
                .stream()
                .map(ViajeMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un viaje específico por su ID.
     */
    @Override
    public ViajeDTO obtenerPorId(Long id) {
        logger.info("Buscando viaje con ID: {}", id);
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con ID: " + id));
        return ViajeMapper.toDTO(viaje);
    }

    /**
     * Crea un nuevo viaje a partir del DTO.
     */
    @Override
    public ViajeDTO crear(ViajeDTO viajeDTO) {
        logger.info("Creando nuevo viaje: {}", viajeDTO.getDestino());
        Empresa empresa = empresaRepository.findById(viajeDTO.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + viajeDTO.getEmpresaId()));
        Viaje viaje = ViajeMapper.toEntity(viajeDTO, empresa);
        Viaje guardado = viajeRepository.save(viaje);
        return ViajeMapper.toDTO(guardado);
    }

    /**
     * Actualiza los datos de un viaje existente.
     */
    @Override
    public ViajeDTO actualizar(Long id, ViajeDTO viajeDTO) {
        logger.info("Actualizando viaje con ID: {}", id);
        Viaje existente = viajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con ID: " + id));
        Empresa empresa = empresaRepository.findById(viajeDTO.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + viajeDTO.getEmpresaId()));

        // Actualizar campos del viaje
        existente.setDestino(viajeDTO.getDestino());
        existente.setPrecioKm(viajeDTO.getPrecioKm());
        existente.setFecha(viajeDTO.getFecha());
        existente.setDistancia(viajeDTO.getDistancia());
        existente.setEmpresa(empresa);

        Viaje actualizado = viajeRepository.save(existente);
        return ViajeMapper.toDTO(actualizado);
    }

    /**
     * Elimina un viaje por su ID.
     */
    @Override
    public void eliminar(Long id) {
        logger.info("Eliminando viaje con ID: {}", id);
        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con ID: " + id));
        viajeRepository.delete(viaje);
    }

    @Override
    public List<ViajeDTO> findByEmpresaId(Long empresaId) {
        // 1️⃣ Traemos las entidades Viaje
        List<Viaje> viajes = viajeRepository.findByEmpresaId(empresaId);

        // 2️⃣ Convertimos a DTOs
        List<ViajeDTO> viajesDTO = viajes.stream()
                .map(ViajeMapper::toDTO) // usamos el mapper aquí
                .collect(Collectors.toList());

        return viajesDTO;
    }


}
