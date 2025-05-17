package com.rigo.control_viajes.service;

import com.rigo.control_viajes.DTO.ViajeDTO;
import com.rigo.control_viajes.mapper.ViajeMapper;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.model.Viaje;
import com.rigo.control_viajes.repository.EmpresaRepository;
import com.rigo.control_viajes.repository.ViajeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViajeServiceImpl implements ViajeService {

    private static final Logger logger = LoggerFactory.getLogger(ViajeServiceImpl.class);

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public Viaje crearViaje(ViajeDTO viajeDTO) {
        logger.info("Creando viaje con datos: {}", viajeDTO);

        Empresa empresa = empresaRepository.findById(viajeDTO.getEmpresaId())
                .orElseThrow(() -> {
                    logger.warn("Empresa no encontrada con id: {}", viajeDTO.getEmpresaId());
                    return new EntityNotFoundException("Empresa no encontrada con id: " + viajeDTO.getEmpresaId());
                });

        Viaje viaje = ViajeMapper.toEntity(viajeDTO, empresa);
        Viaje viajeGuardado = viajeRepository.save(viaje);

        logger.info("Viaje creado exitosamente: {}", viajeGuardado);
        return viajeGuardado;
    }

    @Override
    public List<Viaje> listarViajes() {
        logger.info("Listando todos los viajes");
        return viajeRepository.findAll();
    }

    @Override
    public Viaje editarViaje(Long id, ViajeDTO viajeDTO) {
        logger.info("Editando viaje con ID: {} y datos: {}", id, viajeDTO);

        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Viaje no encontrado con id: {}", id);
                    return new EntityNotFoundException("Viaje no encontrado con id: " + id);
                });

        Empresa empresa = empresaRepository.findById(viajeDTO.getEmpresaId())
                .orElseThrow(() -> {
                    logger.warn("Empresa no encontrada con id: {}", viajeDTO.getEmpresaId());
                    return new EntityNotFoundException("Empresa no encontrada con id: " + viajeDTO.getEmpresaId());
                });

        viaje.setFecha(viajeDTO.getFecha());
        viaje.setPrecioKm(viajeDTO.getPrecioKm());
        viaje.setDistancia(viajeDTO.getDistancia());
        viaje.setEmpresa(empresa);

        Viaje viajeActualizado = viajeRepository.save(viaje);
        logger.info("Viaje actualizado exitosamente: {}", viajeActualizado);

        return viajeActualizado;
    }

    @Override
    public void eliminarViaje(Long id) {
        logger.info("Eliminando viaje con ID: {}", id);

        Viaje viaje = viajeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Viaje no encontrado con id: {}", id);
                    return new EntityNotFoundException("Viaje no encontrado con id: " + id);
                });

        viajeRepository.delete(viaje);
        logger.info("Viaje eliminado con éxito");
    }
}
