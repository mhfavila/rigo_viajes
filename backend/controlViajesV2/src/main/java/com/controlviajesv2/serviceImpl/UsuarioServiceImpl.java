package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.dto.UsuarioDTO;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.EmpresaMapper;
import com.controlviajesv2.mapper.UsuarioMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
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
     * Crea un nuevo usuario a partir del DTO recibido.//AHORA LO HAGO EN LA LCASE AUTHCONTROLLER EN EL METODO REGISTER
     */


    /*@Override
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        logger.info("Creando nuevo usuario: {}", usuarioDTO.getNombre());
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(guardado);
    }

     */



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
        existente.setRoles(usuarioDTO.getRoles());

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

    /**
     * metodo encargado de sacar las empresas asociadas a un usuario
     * @param usuarioId
     * @return
     */
    @Override
    public List<EmpresaDTO> obtenerEmpresasDeUsuario(Long usuarioId) {
        // 1. Obtener quién está haciendo la petición (del Token)
        String usernameActual = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscar al usuario dueño del ID que intentan consultar
        Usuario usuarioSolicitado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 3. EL GUARDIÁN: Si los nombres no coinciden (y no eres admin), ¡FUERA!
        // (Asumo que el username es único según tu entidad Usuario)
        if (!usuarioSolicitado.getNombre().equals(usernameActual)) {
            // Aquí lanzamos un error 403 si intentan espiar
            throw new AccessDeniedException("No tienes permiso para ver los datos de otro usuario.");
        }

        // 4. Si pasa el control, devolvemos los datos
        return empresaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(EmpresaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
