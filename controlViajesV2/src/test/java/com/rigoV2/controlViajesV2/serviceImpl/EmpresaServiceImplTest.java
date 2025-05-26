package com.rigoV2.controlViajesV2.serviceImpl;


import com.rigoV2.controlViajesV2.dto.EmpresaConViajesDTO;
import com.rigoV2.controlViajesV2.dto.EmpresaDTO;
import com.rigoV2.controlViajesV2.dto.UsuarioDTO;
import com.rigoV2.controlViajesV2.entity.Empresa;
import com.rigoV2.controlViajesV2.entity.Usuario;
import com.rigoV2.controlViajesV2.entity.Viaje;
import com.rigoV2.controlViajesV2.exception.ResourceNotFoundException;
import com.rigoV2.controlViajesV2.mapper.UsuarioMapper;
import com.rigoV2.controlViajesV2.repository.EmpresaRepository;
import com.rigoV2.controlViajesV2.repository.UsuarioRepository;
import com.rigoV2.controlViajesV2.repository.ViajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class EmpresaServiceImplTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ViajeRepository viajeRepository;

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    @Captor
    private ArgumentCaptor<Empresa> empresaCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa X");
        empresa.setUsuario(new Usuario());
        empresaRepository.save(empresa);

        Viaje viaje = new Viaje();
        viaje.setDestino("París");
        viaje.setEmpresa(empresa);
        viajeRepository.save(viaje);
    }

    /**
     * Prueba la creación de una empresa cuando el usuario existe.
     * Se verifica que se llama al repositorio para guardar la empresa y
     * que el DTO devuelto tenga el nombre esperado.
     */
    @Test
    public void testCrearEmpresa_Success() {
        // Preparar datos de entrada
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setNombre("Empresa X");
        empresaDTO.setUsuarioId(1L);

        Empresa empresaEntidad = new Empresa();
        empresaEntidad.setNombre("Empresa X");
        empresaEntidad.setUsuario(usuario);
        empresaEntidad.setId(10L);

        // Mockear comportamiento del repositorio
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresaEntidad);

        // Ejecutar el método a probar
        EmpresaDTO resultado = empresaService.crearEmpresa(empresaDTO);

        // Verificar que se guardó la empresa con el usuario correcto
        verify(empresaRepository).save(empresaCaptor.capture());
        Empresa guardada = empresaCaptor.getValue();
        assertEquals("Empresa X", guardada.getNombre());
        assertEquals(usuario, guardada.getUsuario());

        // Verificar resultado
        assertNotNull(resultado);
        assertEquals("Empresa X", resultado.getNombre());
    }

    /**
     * Prueba que lanzar excepción si el usuario no existe al crear empresa.
     */
    @Test
    public void testCrearEmpresa_UsuarioNoEncontrado() {
        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setNombre("Empresa Y");
        empresaDTO.setUsuarioId(99L);

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Se espera que lance ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> {
            empresaService.crearEmpresa(empresaDTO);
        });
    }

    /**
     * Prueba listar todas las empresas devolviendo una lista con datos simulados.
     */
    @Test
    public void testListarEmpresas() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Empresa Z");

        when(empresaRepository.findAll()).thenReturn(List.of(empresa));

        List<EmpresaDTO> empresas = empresaService.listarEmpresas();

        assertNotNull(empresas);
        assertFalse(empresas.isEmpty());
        assertEquals("Empresa Z", empresas.get(0).getNombre());
    }

    /**
     * Prueba obtener empresa por ID cuando existe.
     */
    @Test
    public void testObtenerEmpresaPorId_Existe() {
        Empresa empresa = new Empresa();
        empresa.setId(5L);
        empresa.setNombre("Empresa Existente");

        when(empresaRepository.findById(5L)).thenReturn(Optional.of(empresa));

        EmpresaDTO dto = empresaService.obtenerEmpresaPorId(5L);

        assertNotNull(dto);
        assertEquals("Empresa Existente", dto.getNombre());
    }

    /**
     * Prueba que se lance excepción cuando no existe empresa por ID.
     */
    @Test
    public void testObtenerEmpresaPorId_NoExiste() {
        when(empresaRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            empresaService.obtenerEmpresaPorId(123L);
        });
    }

    /**
     * Prueba actualizar empresa correctamente.
     */
    @Test
    public void testActualizarEmpresa_Success() {
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setId(1L);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(2L);

        Empresa existente = new Empresa();
        existente.setId(10L);
        existente.setNombre("Empresa Old");
        existente.setUsuario(usuarioOriginal);

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setNombre("Empresa New");
        empresaDTO.setUsuarioId(2L);
        empresaDTO.setCif("123456");
        empresaDTO.setDireccion("Dirección Nueva");
        empresaDTO.setTelefono("555-5555");
        empresaDTO.setEmail("empresa@nuevo.com");

        when(empresaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(nuevoUsuario));
        when(empresaRepository.save(any(Empresa.class))).thenAnswer(i -> i.getArgument(0));

        EmpresaDTO actualizado = empresaService.actualizarEmpresa(10L, empresaDTO);

        assertNotNull(actualizado);
        assertEquals("Empresa New", actualizado.getNombre());
        assertEquals("123456", actualizado.getCif());
        assertEquals("Dirección Nueva", actualizado.getDireccion());
        assertEquals("555-5555", actualizado.getTelefono());
        assertEquals("empresa@nuevo.com", actualizado.getEmail());
        assertEquals(2L, actualizado.getUsuarioId());
    }

    /**
     * Prueba que al actualizar empresa con usuarioId inexistente se lance excepción.
     */
    @Test
    public void testActualizarEmpresa_UsuarioNoEncontrado() {
        Empresa existente = new Empresa();
        existente.setId(20L);
        Usuario usuarioOriginal = new Usuario();
        usuarioOriginal.setId(1L);
        existente.setUsuario(usuarioOriginal);

        EmpresaDTO empresaDTO = new EmpresaDTO();
        empresaDTO.setUsuarioId(99L); // Usuario nuevo no existe
        empresaDTO.setNombre("Empresa Actualizada");

        when(empresaRepository.findById(20L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            empresaService.actualizarEmpresa(20L, empresaDTO);
        });
    }

    /**
     * Prueba eliminar empresa existente.
     */
    @Test
    public void testEliminarEmpresa_Success() {
        when(empresaRepository.existsById(1L)).thenReturn(true);

        // No lanza excepción, solo verifica que se llame deleteById
        empresaService.eliminarEmpresa(1L);

        verify(empresaRepository).deleteById(1L);
    }

    /**
     * Prueba que lanzar excepción al eliminar empresa inexistente.
     */
    @Test
    public void testEliminarEmpresa_NoExiste() {
        when(empresaRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            empresaService.eliminarEmpresa(2L);
        });
    }


}
