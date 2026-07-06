package com.controlviajesv2.controller;

import com.controlviajesv2.entity.AuthRequest;
import com.controlviajesv2.entity.AuthResponse;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.mapper.UsuarioMapper;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.security.JwtTokenGenerator;
import com.controlviajesv2.security.service.LoginAttemptService;
import com.controlviajesv2.security.service.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private JwtTokenGenerator jwtTokenGenerator;
    @Mock
    private TokenBlacklistService tokenBlacklistService;
    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_lanzaUnauthorizedSiUsuarioBloqueado() {
        AuthRequest request = new AuthRequest();
        ReflectionTestUtils.setField(request, "nombre", "admin@test.com");
        ReflectionTestUtils.setField(request, "password", "secret");

        when(loginAttemptService.isBlocked("admin@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authController.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("401 UNAUTHORIZED");
    }

    @Test
    void login_devuelveTokenCuandoCredencialesSonValidas() {
        AuthRequest request = new AuthRequest();
        ReflectionTestUtils.setField(request, "nombre", "admin@test.com");
        ReflectionTestUtils.setField(request, "password", "secret");

        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 1L);
        ReflectionTestUtils.setField(usuario, "email", "admin@test.com");

        when(loginAttemptService.isBlocked("admin@test.com")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken("admin@test.com", "secret"));
        when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(usuario));
        when(jwtTokenGenerator.generateToken(usuario)).thenReturn("jwt-token");

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(ReflectionTestUtils.getField(response.getBody(), "token")).isEqualTo("jwt-token");
        verify(loginAttemptService).loginSucceeded("admin@test.com");
    }

    @Test
    void register_devuelveBadRequestSiEmailYaExiste() {
        AuthRequest request = new AuthRequest();
        ReflectionTestUtils.setField(request, "nombre", "demo");
        ReflectionTestUtils.setField(request, "email", "demo@test.com");
        ReflectionTestUtils.setField(request, "password", "123456");

        when(usuarioRepository.existsByEmail("demo@test.com")).thenReturn(true);

        ResponseEntity<?> response = authController.register(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Error: ¡El email ya está en uso!");
    }

    @Test
    void logout_devuelveBadRequestSiNoHayTokenBearer() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseEntity<?> response = authController.logout(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertThat(body.get("error")).isEqualTo("No se proporcionó token válido");
    }
}
