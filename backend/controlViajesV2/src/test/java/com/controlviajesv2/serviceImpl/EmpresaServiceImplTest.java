package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.EmpresaDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.mapper.EmpresaMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceImplTest {

    @Mock
    private EmpresaMapper empresaMapper;
    @Mock
    private EmpresaRepository empresaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin@test.com", "pwd")
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listarEmpresas_filtraPorUsuarioAutenticado() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 10L);
        ReflectionTestUtils.setField(usuario, "email", "admin@test.com");

        Empresa empresa = new Empresa();
        ReflectionTestUtils.setField(empresa, "id", 1L);
        ReflectionTestUtils.setField(empresa, "nombre", "Acme");
        ReflectionTestUtils.setField(empresa, "usuario", usuario);

        EmpresaDTO dto = new EmpresaDTO();
        ReflectionTestUtils.setField(dto, "id", 1L);
        ReflectionTestUtils.setField(dto, "nombre", "Acme");

        when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(usuario));
        when(empresaRepository.findByUsuario(usuario)).thenReturn(List.of(empresa));
        when(empresaMapper.toDTO(empresa)).thenReturn(dto);

        List<EmpresaDTO> resultado = empresaService.listarEmpresas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isSameAs(dto);
        verify(empresaRepository).findByUsuario(usuario);
    }

    @Test
    void crearEmpresa_usaUsuarioAutenticadoParaPersistir() {
        Usuario usuario = new Usuario();
        ReflectionTestUtils.setField(usuario, "id", 10L);
        ReflectionTestUtils.setField(usuario, "email", "admin@test.com");

        EmpresaDTO input = new EmpresaDTO();
        ReflectionTestUtils.setField(input, "nombre", "Nueva Empresa");

        Empresa entidad = new Empresa();
        ReflectionTestUtils.setField(entidad, "nombre", "Nueva Empresa");
        ReflectionTestUtils.setField(entidad, "usuario", usuario);

        Empresa guardada = new Empresa();
        ReflectionTestUtils.setField(guardada, "id", 99L);
        ReflectionTestUtils.setField(guardada, "nombre", "Nueva Empresa");
        ReflectionTestUtils.setField(guardada, "usuario", usuario);

        EmpresaDTO salida = new EmpresaDTO();
        ReflectionTestUtils.setField(salida, "id", 99L);
        ReflectionTestUtils.setField(salida, "nombre", "Nueva Empresa");

        when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(usuario));
        when(empresaMapper.toEntity(input, usuario)).thenReturn(entidad);
        when(empresaRepository.save(entidad)).thenReturn(guardada);
        when(empresaMapper.toDTO(guardada)).thenReturn(salida);

        EmpresaDTO resultado = empresaService.crearEmpresa(input);

        assertThat(resultado).isSameAs(salida);
        assertThat(ReflectionTestUtils.getField(resultado, "id")).isEqualTo(99L);
        verify(empresaMapper).toEntity(input, usuario);
        verify(empresaRepository).save(entidad);
        verify(empresaMapper).toDTO(guardada);
    }
}
