package com.rigo.control_viajes.service;


import com.rigo.control_viajes.DTO.EmpresaDTO;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de prueba unitaria para {@link EmpresaServiceImpl}.
 * Utiliza JUnit y Mockito para verificar el comportamiento del servicio.
 */
public class EmpresaServiceImplTest {

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    @Mock
    private EmpresaRepository empresaRepository;

    private Empresa empresa;

    /**
     * Inicializa los mocks y crea una instancia de empresa de prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks de Mockito

        // Creación de una empresa de ejemplo para usar en los tests
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("EmpresaTest");
        empresa.setCif("A12345678");
        empresa.setTelefono("123456789");
        empresa.setEmail("correo@test.com");
    }

    /**
     * Verifica que el método crearEmpresa guarde correctamente la entidad.
     */
    @Test
    void crearEmpresa_deberiaGuardarYDevolverEmpresa() {
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        Empresa resultado = empresaService.crearEmpresa(empresa);

        assertNotNull(resultado);
        assertEquals("EmpresaTest", resultado.getNombre());
        verify(empresaRepository).save(empresa);
    }

    /**
     * Verifica que listarEmpresas devuelva una lista con las empresas existentes.
     */
    @Test
    void listarEmpresas_deberiaDevolverLista() {
        List<Empresa> lista = List.of(empresa);
        when(empresaRepository.findAll()).thenReturn(lista);

        List<Empresa> resultado = empresaService.listarEmpresas();

        assertEquals(1, resultado.size());
        verify(empresaRepository).findAll();
    }

    /**
     * Verifica que obtenerEmpresaConViajes retorne la empresa si existe.
     */
    @Test
    void obtenerEmpresaConViajes_existente_deberiaDevolverEmpresa() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        Empresa resultado = empresaService.obtenerEmpresaConViajes(1L);

        assertEquals("EmpresaTest", resultado.getNombre());
        verify(empresaRepository).findById(1L);
    }

    /**
     * Verifica que obtenerEmpresaConViajes lance una excepción si la empresa no existe.
     */
    @Test
    void obtenerEmpresaConViajes_noExistente_deberiaLanzarExcepcion() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            empresaService.obtenerEmpresaConViajes(1L);
        });

        assertEquals("Empresa no encontrada con ID: 1", exception.getMessage());
    }

    /**
     * Verifica que editarEmpresa actualice correctamente los datos de la empresa.
     */
    @Test
    void editarEmpresa_deberiaActualizarDatos() {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setNombre("NuevoNombre");
        dto.setCif("B12345678");
        dto.setTelefono("987654321");
        dto.setEmail("nuevo@correo.com");

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        Empresa resultado = empresaService.editarEmpresa(1L, dto);

        assertEquals("NuevoNombre", resultado.getNombre());
        assertEquals("nuevo@correo.com", resultado.getEmail());
    }

    /**
     * Verifica que eliminarEmpresa elimine la empresa si existe.
     */
    @Test
    void eliminarEmpresa_existente_deberiaEliminar() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        empresaService.eliminarEmpresa(1L);

        verify(empresaRepository).delete(empresa);
    }

    /**
     * Verifica que eliminarEmpresa lance una excepción si la empresa no existe.
     */
    @Test
    void eliminarEmpresa_noExistente_deberiaLanzarExcepcion() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            empresaService.eliminarEmpresa(1L);
        });

        assertEquals("Empresa no encontrada con ID: 1", exception.getMessage());
    }
}