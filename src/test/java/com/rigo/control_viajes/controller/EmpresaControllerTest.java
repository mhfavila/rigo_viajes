package com.rigo.control_viajes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigo.control_viajes.DTO.EmpresaDTO;
import com.rigo.control_viajes.security.SecurityConfigTest;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.service.EmpresaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para {@link EmpresaController}.
 * Utiliza Spring MockMvc para simular solicitudes HTTP y verificar respuestas del controlador.
 */
@WebMvcTest(EmpresaController.class)
@AutoConfigureMockMvc
@Import(SecurityConfigTest.class)  // Importamos la configuración de seguridad para tests
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresaService empresaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa(1L, "EmpresaTest", "A12345678", "123456789", "correo@test.com");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void crearEmpresa_deberiaRetornarCreada() throws Exception {
        EmpresaDTO dto = new EmpresaDTO("EmpresaTest", "A12345678", "123456789", "correo@test.com");

        Mockito.when(empresaService.crearEmpresa(Mockito.any(Empresa.class))).thenReturn(empresa);

        mockMvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje", is("Empresa creada con éxito")))
                .andExpect(jsonPath("$.data.nombre", is("EmpresaTest")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listarEmpresas_deberiaRetornarLista() throws Exception {
        Mockito.when(empresaService.listarEmpresas()).thenReturn(List.of(empresa));

        mockMvc.perform(get("/api/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Empresas obtenidas con éxito")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].nombre", is("EmpresaTest")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void obtenerEmpresaConViajes_deberiaRetornarEmpresa() throws Exception {
        Mockito.when(empresaService.obtenerEmpresaConViajes(1L)).thenReturn(empresa);

        mockMvc.perform(get("/api/empresas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("EmpresaTest")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editarEmpresa_deberiaActualizarYRetornarEmpresa() throws Exception {
        EmpresaDTO dto = new EmpresaDTO("EmpresaEditada", "B12345678", "987654321", "nuevo@correo.com");
        Empresa empresaEditada = new Empresa(1L, dto.getNombre(), dto.getCif(), dto.getTelefono(), dto.getEmail());

        Mockito.when(empresaService.editarEmpresa(Mockito.eq(1L), Mockito.any(EmpresaDTO.class))).thenReturn(empresaEditada);

        mockMvc.perform(put("/api/empresas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Empresa actualizada con éxito")))
                .andExpect(jsonPath("$.data.nombre", is("EmpresaEditada")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void eliminarEmpresa_deberiaEliminarYConfirmar() throws Exception {
        Mockito.doNothing().when(empresaService).eliminarEmpresa(1L);

        mockMvc.perform(delete("/api/empresas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Empresa eliminada con éxito")));
    }

}