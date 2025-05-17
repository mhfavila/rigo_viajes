package com.rigo.control_viajes.service;

import com.rigo.control_viajes.DTO.EmpresaDTO;
import com.rigo.control_viajes.controller.EmpresaController;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmpresaServiceImpl implements EmpresaService{
    private static final Logger logger = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public Empresa crearEmpresa(Empresa empresa) {
        logger.info("Creando empresa: {}", empresa);
        return empresaRepository.save(empresa);
    }

    @Override
    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll();
    }

    @Override
    public Empresa obtenerEmpresaConViajes(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con ID: " + id));
    }

    @Override
    public Empresa editarEmpresa(Long id, EmpresaDTO empresaDTO) {
        logger.info("Editando empresa con ID: {} y datos: {}", id, empresaDTO);
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con ID: " + id));
        empresa.setNombre(empresaDTO.getNombre());
        empresa.setCif(empresaDTO.getCif());
        empresa.setTelefono(empresaDTO.getTelefono());
        empresa.setEmail(empresaDTO.getEmail());

        return empresaRepository.save(empresa);
    }

    @Override
    public void eliminarEmpresa(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con ID: " + id));

        logger.info("Eliminando empresa con ID: {}", id);
        empresaRepository.delete(empresa);
    }
}
