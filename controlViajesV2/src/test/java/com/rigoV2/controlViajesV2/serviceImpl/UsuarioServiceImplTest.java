package com.rigoV2.controlViajesV2.serviceImpl;


import com.rigoV2.controlViajesV2.dto.UsuarioDTO;
import com.rigoV2.controlViajesV2.entity.Usuario;
import com.rigoV2.controlViajesV2.exception.ResourceNotFoundException;
import com.rigoV2.controlViajesV2.mapper.UsuarioMapper;
import com.rigoV2.controlViajesV2.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Anotación para integrar Mockito con JUnit 5
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    // Mock del repositorio para simular la capa de datos
    @Mock
    private UsuarioRepository usuarioRepository;

    // Clase que vamos a probar, con el mock inyectado automáticamente
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    // Variables auxiliares para pruebas
    private Usuario usuarioEjemplo;
    private UsuarioDTO usuarioDTOEjemplo;

    // Se ejecuta antes de cada test para preparar los datos comunes
    @BeforeEach
    void setUp() {
        // Creamos una entidad Usuario con datos de ejemplo
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setNombre("Juan");
        usuarioEjemplo.setEmail("juan@email.com");
        usuarioEjemplo.setPassword("123456");

        // Convertimos la entidad a DTO para usar en las pruebas
        usuarioDTOEjemplo = UsuarioMapper.toDTO(usuarioEjemplo);
    }

    // Test para el método obtenerTodos()
    @Test
    void obtenerTodos_DeberiaRetornarListaDeDTOs() {
        // Configuramos el mock para que retorne una lista con nuestro usuario de ejemplo
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        // Llamamos al método que queremos probar
        List<UsuarioDTO> resultado = usuarioService.obtenerTodos();

        // Verificamos que el resultado no sea nulo ni vacío y que los datos coincidan
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(usuarioEjemplo.getNombre(), resultado.get(0).getNombre());

        // Verificamos que se haya llamado exactamente una vez al método findAll del repositorio
        verify(usuarioRepository, times(1)).findAll();
    }

    // Test para obtenerPorId() cuando el usuario existe
    @Test
    void obtenerPorId_CuandoExiste_DeberiaRetornarDTO() {
        // Simulamos que el repositorio encuentra el usuario con id 1
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        // Llamamos al método
        UsuarioDTO resultado = usuarioService.obtenerPorId(1L);

        // Comprobamos que el resultado no sea nulo y que coincida el id
        assertNotNull(resultado);
        assertEquals(usuarioEjemplo.getId(), resultado.getId());

        // Verificamos que findById se haya invocado una vez
        verify(usuarioRepository, times(1)).findById(1L);
    }

    // Test para obtenerPorId() cuando el usuario NO existe (lanza excepción)
    @Test
    void obtenerPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Configuramos el mock para que no encuentre el usuario (retorna Optional vacío)
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Verificamos que el método lance la excepción esperada
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerPorId(1L));

        // Verificamos la invocación a findById
        verify(usuarioRepository, times(1)).findById(1L);
    }

    // Test para crear un nuevo usuario
    @Test
    void crear_DeberiaGuardarYRetornarDTO() {
        // Configuramos el mock para que al guardar retorne nuestro usuario de ejemplo
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        // Llamamos al método crear con el DTO de ejemplo
        UsuarioDTO resultado = usuarioService.crear(usuarioDTOEjemplo);

        // Verificamos que el resultado no sea nulo y que los datos coincidan
        assertNotNull(resultado);
        assertEquals(usuarioEjemplo.getNombre(), resultado.getNombre());

        // Verificamos que se llamó al método save exactamente una vez
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // Test para actualizar usuario cuando existe
    @Test
    void actualizar_CuandoExiste_DeberiaActualizarYRetornarDTO() {
        // Simulamos que el usuario existe en el repositorio
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));
        // Simulamos que al guardar, retorna el usuario actualizado
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        // Creamos un DTO con datos nuevos para la actualización
        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setNombre("Juan Actualizado");
        usuarioActualizado.setEmail("juan.act@email.com");
        usuarioActualizado.setPassword("newpass");

        // Llamamos al método actualizar
        UsuarioDTO resultado = usuarioService.actualizar(1L, usuarioActualizado);

        // Verificamos que el resultado no sea nulo y que el nombre fue actualizado
        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());

        // Verificamos las llamadas a findById y save
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // Test para actualizar usuario cuando no existe (lanza excepción)
    @Test
    void actualizar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Simulamos que no se encuentra el usuario
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setNombre("Juan Actualizado");

        // Verificamos que se lance excepción
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.actualizar(1L, usuarioActualizado));

        // Verificamos que se haya llamado findById pero no save
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).save(any());
    }

    // Test para eliminar usuario cuando existe
    @Test
    void eliminar_CuandoExiste_DeberiaEliminar() {
        // Simulamos que el usuario existe
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));
        doNothing().when(usuarioRepository).delete(usuarioEjemplo);

        // Llamamos al método eliminar
        usuarioService.eliminar(1L);

        // Verificamos llamadas a findById y delete
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).delete(usuarioEjemplo);
    }

    // Test para eliminar usuario cuando no existe (lanza excepción)
    @Test
    void eliminar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        // Simulamos que no se encuentra el usuario
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Verificamos que se lance la excepción
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.eliminar(1L));

        // Verificamos llamadas a findById y que delete no se invoque
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).delete(any());
    }
}
