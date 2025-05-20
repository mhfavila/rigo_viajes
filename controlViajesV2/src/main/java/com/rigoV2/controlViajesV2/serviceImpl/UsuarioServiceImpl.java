package com.rigoV2.controlViajesV2.serviceImpl;

import com.rigoV2.controlViajesV2.dto.UsuarioDTO;
import com.rigoV2.controlViajesV2.entity.Usuario;
import com.rigoV2.controlViajesV2.exception.ResourceNotFoundException;
import com.rigoV2.controlViajesV2.mapper.UsuarioMapper;
import com.rigoV2.controlViajesV2.repository.UsuarioRepository;
import com.rigoV2.controlViajesV2.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todos los usuarios existentes en la base de datos.
     */
    @Override
    public List<UsuarioDTO> obtenerTodos() {
        logger.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por su ID.
     * Lanza excepción si no se encuentra.
     */
    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        logger.info("Buscando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return UsuarioMapper.toDTO(usuario);
    }

    /**
     * Crea un nuevo usuario a partir del DTO recibido.
     */
    @Override
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        logger.info("Creando nuevo usuario: {}", usuarioDTO.getNombre());
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(guardado);
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    @Override
    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        logger.info("Actualizando usuario con ID: {}", id);
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Se actualizan los campos del usuario
        existente.setNombre(usuarioDTO.getNombre());
        existente.setEmail(usuarioDTO.getEmail());
        existente.setPassword(usuarioDTO.getPassword());

        Usuario actualizado = usuarioRepository.save(existente);
        return UsuarioMapper.toDTO(actualizado);
    }

    /**
     * Elimina un usuario por su ID.
     */
    @Override
    public void eliminar(Long id) {
        logger.info("Eliminando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuarioRepository.delete(usuario);
    }

}
