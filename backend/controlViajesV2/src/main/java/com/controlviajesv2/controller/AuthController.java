package com.controlviajesv2.controller;


import com.controlviajesv2.entity.AuthRequest;
import com.controlviajesv2.entity.AuthResponse;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.mapper.UsuarioMapper;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.security.JwtTokenGenerator;
import com.controlviajesv2.security.service.LoginAttemptService;
import com.controlviajesv2.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.controlviajesv2.security.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")  // Permite peticiones desde Angular
@RestController
@RequestMapping(AppConstants.REQUEST_AUTHCONTROLLER)
@Tag(name = "Autenticación", description = "Endpoints para registro e inicio de sesión de usuarios")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final TokenBlacklistService tokenBlacklistService;
    private final LoginAttemptService loginAttemptService;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper, JwtTokenGenerator jwtTokenGenerator, TokenBlacklistService tokenBlacklistService, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.tokenBlacklistService = tokenBlacklistService;
        this.loginAttemptService = loginAttemptService;
    }

    /**
     * Autentica a un usuario y devuelve un JWT si las credenciales son válidas.
     */
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con email y contraseña y devuelve un Token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso (Token generado)"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping(AppConstants.REQUEST_AUTHCONTROLLER_LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        //el request.getNombre() esta recibiendo el email ,no el nombre ,esta asi por que desde el fronted se llama nombre
        logger.info("Intento de login para el usuario: {}", request.getNombre());
        // verificar si el usuario esta bloqueado
        if (loginAttemptService.isBlocked(request.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario bloqueado por demasiados intentos. Inténtalo en 15 minutos."
            );
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getNombre(), request.getPassword())
            );
            //si llega aqui limpiamos el contador
            loginAttemptService.loginSucceeded(request.getNombre());

            Usuario usuario = usuarioRepository.findByEmail(request.getNombre())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            String token = jwtTokenGenerator.generateToken(usuario);

            return ResponseEntity.ok(new AuthResponse(token));

        }catch (BadCredentialsException e){
            loginAttemptService.loginFailed(request.getNombre());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas"
            );
        }
    }

    /**
     * Registra un nuevo usuario si el nombre no está ya en uso.
     */
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario. El email debe ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El email ya existe o datos inválidos")
    })
    @PostMapping(AppConstants.REQUEST_AUTHCONTROLLER_REGISTER)
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        logger.info("Intento de registro para el usuario: {}", request.getEmail());
        // Comprobamos explícitamente que la contraseña venga rellena y tenga longitud mínima.
        if (request.getPassword() == null || request.getPassword().trim().length() < 6) {
            // Si falla, lanzamos un error que impedirá el registro
            throw new IllegalArgumentException("La contraseña es obligatoria y debe tener al menos 6 caracteres para registrarse");
        }


        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: ¡El email ya está en uso!");
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
        roles.add("ROLE_USER");// Forzamos siempre el rol básico al registrarse

       // roles.add(request.getRol());
        nuevoUsuario.setRoles(roles);

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));



    }

    /**Cierre de sesión (Logout)
     * Invalida el token actual añadiéndolo a la lista negra.
     */
    @Operation(summary = "Cerrar Sesion", description = "Cierra la seesion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El email ya existe o datos inválidos")
    })
    @PostMapping(AppConstants.REQUEST_AUTHCONTROLLER_LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Invalidar el token
            tokenBlacklistService.revokeToken(token);
            logger.info("Token revocado correctamente. Logout exitoso.");

            return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada correctamente"));
        }

        return ResponseEntity.badRequest().body(Map.of("error", "No se proporcionó token válido"));
    }
}
