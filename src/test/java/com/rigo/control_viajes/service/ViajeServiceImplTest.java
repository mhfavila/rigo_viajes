package com.rigo.control_viajes.service;



import com.rigo.control_viajes.DTO.ViajeDTO;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.model.Viaje;
import com.rigo.control_viajes.repository.EmpresaRepository;
import com.rigo.control_viajes.repository.ViajeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ViajeServiceImplTest {

    // Simula la dependencia al repositorio de viajes
    @Mock
    private ViajeRepository viajeRepository;

    // Simula la dependencia al repositorio de empresas
    @Mock
    private EmpresaRepository empresaRepository;

    // Inyecta los mocks en el servicio a probar
    @InjectMocks
    private ViajeServiceImpl viajeService;

    private Empresa empresa;
    private Viaje viaje;
    private ViajeDTO viajeDTO;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        // Crea datos de prueba
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("EmpresaTest");

        viaje = new Viaje();
        viaje.setId(1L);
        viaje.setFecha(LocalDate.now());
        viaje.setPrecioKm(1.5);
        viaje.setDistancia(100.0);
        viaje.setEmpresa(empresa);

        viajeDTO = new ViajeDTO(LocalDate.now(), 1.5, 100.0, 1L);
    }

    @Test
    void crearViaje_deberiaCrearViajeCorrectamente() {
        // Simula búsqueda de empresa existente
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        // Simula guardado del viaje
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viaje);

        // Ejecuta el método a probar
        Viaje resultado = viajeService.crearViaje(viajeDTO);

        // Verifica que se creó correctamente
        assertNotNull(resultado);
        assertEquals(viaje.getPrecioKm(), resultado.getPrecioKm());
        verify(viajeRepository).save(any(Viaje.class));
    }

    @Test
    void crearViaje_empresaNoExiste_deberiaLanzarExcepcion() {
        // Simula que la empresa no existe
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica que se lanza excepción
        assertThrows(EntityNotFoundException.class, () -> viajeService.crearViaje(viajeDTO));
    }

    @Test
    void listarViajes_deberiaRetornarListaDeViajes() {
        // Simula lista de viajes
        when(viajeRepository.findAll()).thenReturn(List.of(viaje));

        // Ejecuta el método
        List<Viaje> resultado = viajeService.listarViajes();

        // Verifica resultados
        assertEquals(1, resultado.size());
        verify(viajeRepository).findAll();
    }

    @Test
    void editarViaje_deberiaActualizarViajeCorrectamente() {
        // Simula búsqueda y actualización del viaje
        when(viajeRepository.findById(1L)).thenReturn(Optional.of(viaje));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(viajeRepository.save(any(Viaje.class))).thenReturn(viaje);

        // Ejecuta el método
        Viaje actualizado = viajeService.editarViaje(1L, viajeDTO);

        // Verifica actualización
        assertNotNull(actualizado);
        assertEquals(viajeDTO.getPrecioKm(), actualizado.getPrecioKm());
        verify(viajeRepository).save(any(Viaje.class));
    }

    @Test
    void editarViaje_viajeNoExiste_deberiaLanzarExcepcion() {
        when(viajeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> viajeService.editarViaje(1L, viajeDTO));
    }

    @Test
    void eliminarViaje_deberiaEliminarViajeCorrectamente() {
        when(viajeRepository.findById(1L)).thenReturn(Optional.of(viaje));

        viajeService.eliminarViaje(1L);

        verify(viajeRepository).delete(viaje);
    }

    @Test
    void eliminarViaje_viajeNoExiste_deberiaLanzarExcepcion() {
        when(viajeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> viajeService.eliminarViaje(1L));
    }
}

