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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmpresaMapper empresaMapper;

    @Override
    public UsuarioDTO obtenerUsuarioActual() {
        // 1. Obtenemos el email del usuario logueado desde el Token de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 2. Buscamos en la BD por ese email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return usuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO) {
        // 1. Obtenemos al usuario real que está haciendo la petición
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 2. Actualizamos solo los datos permitidos
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setNif(usuarioDTO.getNif());

        usuario.setCuentaBancaria(usuarioDTO.getCuentaBancaria());
        // 3. ACTUALIZACIÓN DE DIRECCIÓN (CORREGIDO)
        // Primero verificamos si el usuario ya tiene una dirección instanciada
        if (usuario.getDireccion() == null) {
            usuario.setDireccion(new com.controlviajesv2.entity.Direccion());
        }

        // Ahora actualizamos los campos DENTRO del objeto dirección
        // Nota: Asumo que en tu clase Direccion.java los campos se llaman 'calle', 'numero', etc.
        // Si se llaman diferente, cámbialo aquí.
        usuario.getDireccion().setCalle(usuarioDTO.getDireccion().getCalle());
        usuario.getDireccion().setNumero(usuarioDTO.getDireccion().getNumero());
        usuario.getDireccion().setCiudad(usuarioDTO.getDireccion().getCiudad());
        usuario.getDireccion().setProvincia(usuarioDTO.getDireccion().getProvincia());
        usuario.getDireccion().setPais(usuarioDTO.getDireccion().getPais());
        usuario.getDireccion().setCodigoPostal(usuarioDTO.getDireccion().getCodigoPostal());

        // Si envía contraseña nueva, la encriptamos y actualizamos
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(actualizado);
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return usuarioMapper.toDTO(usuario);
    }


}