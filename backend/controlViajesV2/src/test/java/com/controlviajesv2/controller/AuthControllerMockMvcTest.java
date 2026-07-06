package com.controlviajesv2.controller;

import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.mapper.UsuarioMapper;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.security.JWTAuthenticationFilter;
import com.controlviajesv2.security.JwtTokenGenerator;
import com.controlviajesv2.security.service.LoginAttemptService;
import com.controlviajesv2.security.service.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UsuarioMapper usuarioMapper;
    @MockBean
    private JwtTokenGenerator jwtTokenGenerator;
    @MockBean
    private TokenBlacklistService tokenBlacklistService;
    @MockBean
    private LoginAttemptService loginAttemptService;
    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void login_devuelve401_siUsuarioBloqueado() throws Exception {
        when(loginAttemptService.isBlocked("admin@test.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"admin@test.com\",\"password\":\"secret\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_devuelve200_yToken_siCredencialesValidas() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("admin@test.com");

        when(loginAttemptService.isBlocked("admin@test.com")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken("admin@test.com", "secret"));
        when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(usuario));
        when(jwtTokenGenerator.generateToken(usuario)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"admin@test.com\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(loginAttemptService).loginSucceeded("admin@test.com");
    }

    @Test
    void register_devuelve400_siEmailExiste() throws Exception {
        when(usuarioRepository.existsByEmail("demo@test.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"demo\",\"email\":\"demo@test.com\",\"password\":\"123456\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void logout_devuelve200_siHayBearerToken() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer token-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Sesión cerrada correctamente"));

        verify(tokenBlacklistService).blacklistToken("token-123");
    }
}
