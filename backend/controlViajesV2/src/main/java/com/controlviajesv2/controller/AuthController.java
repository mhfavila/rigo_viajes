package com.controlviajesv2.controller;


import com.controlviajesv2.entity.AuthRequest;
import com.controlviajesv2.entity.AuthResponse;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.mapper.UsuarioMapper;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.security.JwtTokenGenerator;
import com.controlviajesv2.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")  // Permite peticiones desde Angular
@RestController
@RequestMapping(AppConstants.REQUEST_AUTHCONTROLLER)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper, JwtTokenGenerator jwtTokenGenerator) {
        this.authenticationManager = authenticationManager;

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    /**
     * Autentica a un usuario y devuelve un JWT si las credenciales son válidas.
     */

    @PostMapping(AppConstants.REQUEST_AUTHCONTROLLER_LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("Intento de login para el usuario: {}", request.getNombre());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNombre(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByNombre(request.getNombre())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtTokenGenerator.generateToken(usuario.getNombre());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Registra un nuevo usuario si el nombre no está ya en uso.(TENER CUIDADO POR QUE TENGO DOS FORMAS DE CREAR USUARIOS ACTUALMETE SE USA ESTE)
     */
    // REGISTRO
    @PostMapping(AppConstants.REQUEST_AUTHCONTROLLER_REGISTER)
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        logger.info("Intento de registro para el usuario: {}", request.getNombre());

        // Verificar si ya existe usuario con ese nombre
        if (usuarioRepository.findByNombre(request.getNombre()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: El nombre de usuario ya está en uso");
        }

        // Crear nuevo usuario con password cifrada
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
       // nuevoUsuario.setEmail(request.getNombre() + "@tudominio.com"); // Si quieres, puedes pedir email en otro DTO
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asignar rol por defecto
        Set<String> roles = new HashSet<>();
        //roles.add("USER");
        roles.add(request.getRol());
        nuevoUsuario.setRoles(roles);

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));



    }
}
