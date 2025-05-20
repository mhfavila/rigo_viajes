package com.rigoV2.controlViajesV2.serviceImpl;

import com.rigoV2.controlViajesV2.dto.EmpresaDTO;
import com.rigoV2.controlViajesV2.entity.Empresa;
import com.rigoV2.controlViajesV2.entity.Usuario;
import com.rigoV2.controlViajesV2.exception.ResourceNotFoundException;
import com.rigoV2.controlViajesV2.mapper.EmpresaMapper;
import com.rigoV2.controlViajesV2.repository.EmpresaRepository;
import com.rigoV2.controlViajesV2.repository.UsuarioRepository;
import com.rigoV2.controlViajesV2.service.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaServiceImpl.class);


    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public EmpresaDTO crearEmpresa(EmpresaDTO empresaDTO) {

        logger.info("Creando nueva empresa con nombre: {}", empresaDTO.getNombre());
        // Obtenemos el usuario asociado usando el ID del DTO
        Usuario usuario = usuarioRepository.findById(empresaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + empresaDTO.getUsuarioId()));
        logger.info("Creando nueva empresa con nombre: {} para el usuario: {}", empresaDTO.getNombre(),usuario.getNombre());
        // Convertimos el DTO a entidad y asociamos el usuario
        Empresa empresa = EmpresaMapper.toEntity(empresaDTO,usuario);


        // Guardamos la empresa y convertimos la entidad guardada en un DTO para devolverla
        Empresa guardada = empresaRepository.save(empresa);
        return EmpresaMapper.toDTO(guardada);
    }

    @Override
    public List<EmpresaDTO> listarEmpresas() {
        // Obtenemos todas las empresas y las convertimos a DTOs
        logger.info("Obteniendo todas las empresas");
        return empresaRepository.findAll().stream()
                .map(EmpresaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmpresaDTO obtenerEmpresaPorId(Long id) {
        // Buscamos la empresa por ID o lanzamos excepción si no existe
        logger.info("Buscando empresa con ID: {}", id);
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));
        return EmpresaMapper.toDTO(empresa);
    }

    @Override
    public EmpresaDTO actualizarEmpresa(Long id, EmpresaDTO empresaDTO) {
        logger.info("Actualizando empresa con ID: {}", id);
        // Obtenemos la empresa existente por ID
        Empresa existente = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));

        // Si el usuarioId cambia, buscamos el nuevo usuario y lo asociamos
        if (!empresaDTO.getUsuarioId().equals(existente.getUsuario().getId())) {
            Usuario nuevoUsuario = usuarioRepository.findById(empresaDTO.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + empresaDTO.getUsuarioId()));
            existente.setUsuario(nuevoUsuario);
        }

        // Actualizamos los datos básicos
        existente.setNombre(empresaDTO.getNombre());
        existente.setCif(empresaDTO.getCif());
        existente.setDireccion(empresaDTO.getDireccion());
        existente.setTelefono(empresaDTO.getTelefono());
        existente.setEmail(empresaDTO.getEmail());

        // Guardamos y devolvemos el resultado actualizado
        Empresa actualizada = empresaRepository.save(existente);
        return EmpresaMapper.toDTO(actualizada);
    }

    @Override
    public void eliminarEmpresa(Long id) {
        logger.info("Eliminando empresa con ID: {}", id);
        // Verificamos si la empresa existe antes de eliminarla
        if (!empresaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empresa no encontrada con ID: " + id);
        }
        empresaRepository.deleteById(id);
    }
}
