package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.entity.Direccion;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.EmpresaMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.service.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    private EmpresaMapper empresaMapper;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public EmpresaDTO crearEmpresa(EmpresaDTO empresaDTO) {
        // 1. Obtener usuario autenticado (igual que en listarEmpresas)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        logger.info("Creando empresa {} para el usuario autenticado: {}", empresaDTO.getNombre(), email);

        // 2. Usamos el usuario REAL, ignorando lo que venga en el DTO
        Empresa empresa = empresaMapper.toEntity(empresaDTO, usuario);

        Empresa guardada = empresaRepository.save(empresa);
        return empresaMapper.toDTO(guardada);
    }

    @Override
    public List<EmpresaDTO> listarEmpresas() {
            // 1. Averiguamos quién es el usuario logueado (sacamos su email del Token)
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            logger.info("Listando empresas para el usuario: {}", email);

            // 2. Buscamos al usuario completo en la BD
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // 3. 🛑 AQUÍ ESTÁ EL CAMBIO: Usamos 'findByUsuario' en vez de 'findAll'
            return empresaRepository.findByUsuario(usuario).stream()
                    .map(empresaMapper::toDTO)
                    .collect(Collectors.toList());
        }


    @Override
    public EmpresaDTO obtenerEmpresaPorId(Long id) {
        // Buscamos la empresa por ID o lanzamos excepción si no existe
        logger.info("Buscando empresa con ID: {}", id);
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));
        return empresaMapper.toDTO(empresa);
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
        if (empresaDTO.getDireccion() != null) {
            
            Direccion nuevaDireccion = new Direccion();
            nuevaDireccion.setCalle(empresaDTO.getDireccion().getCalle());
            nuevaDireccion.setNumero(empresaDTO.getDireccion().getNumero());
            nuevaDireccion.setCodigoPostal(empresaDTO.getDireccion().getCodigoPostal());
            nuevaDireccion.setCiudad(empresaDTO.getDireccion().getCiudad());
            nuevaDireccion.setProvincia(empresaDTO.getDireccion().getProvincia());
            nuevaDireccion.setPais(empresaDTO.getDireccion().getPais());

            existente.setDireccion(nuevaDireccion);
        }
        existente.setTelefono(empresaDTO.getTelefono());
        existente.setEmail(empresaDTO.getEmail());

        // Guardamos y devolvemos el resultado actualizado
        Empresa actualizada = empresaRepository.save(existente);
        return empresaMapper.toDTO(actualizada);
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
