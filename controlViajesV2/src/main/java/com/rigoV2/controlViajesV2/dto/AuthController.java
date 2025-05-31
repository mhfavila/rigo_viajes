package com.rigoV2.controlViajesV2.dto;


import com.rigoV2.controlViajesV2.entity.Usuario;
import com.rigoV2.controlViajesV2.mapper.UsuarioMapper;
import com.rigoV2.controlViajesV2.repository.UsuarioRepository;
import com.rigoV2.controlViajesV2.security.JWTUtil;
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
import java.util.Set;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    /**
     * Autentica a un usuario y devuelve un JWT si las credenciales son válidas.
     */

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("Intento de login para el usuario: {}", request.getNombre());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNombre(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByNombre(request.getNombre())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(usuario.getNombre());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Registra un nuevo usuario si el nombre no está ya en uso.
     */
    // REGISTRO
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        logger.info("Intento de registro para el usuario: {}", request.getNombre());

        // Verificar si ya existe usuario con ese nombre
        if (usuarioRepository.findByNombre(request.getNombre()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: El nombre de usuario ya está en uso");
        }

        // Crear nuevo usuario con password cifrada y rol USER por defecto
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
       // nuevoUsuario.setEmail(request.getNombre() + "@tudominio.com"); // Si quieres, puedes pedir email en otro DTO
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asignar rol por defecto
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        nuevoUsuario.setRoles(roles);

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
