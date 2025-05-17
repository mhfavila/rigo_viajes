package com.rigo.control_viajes.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigo.control_viajes.DTO.ViajeDTO;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.model.Viaje;
import com.rigo.control_viajes.service.ViajeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ViajeController.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class ViajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ViajeService viajeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Empresa empresa;  // <-- Declarar empresa aquí
    private Viaje viaje;
    private ViajeDTO viajeDTO;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Empresa Test");

        viajeDTO = new ViajeDTO();
        viajeDTO.setFecha(LocalDate.now());
        viajeDTO.setDistancia(100.0);
        viajeDTO.setPrecioKm(0.5);
        viajeDTO.setEmpresaId(1L);

        viaje = new Viaje();
        viaje.setId(1L);
        viaje.setFecha(viajeDTO.getFecha());
        viaje.setDistancia(viajeDTO.getDistancia());
        viaje.setPrecioKm(viajeDTO.getPrecioKm());
        viaje.setEmpresa(empresa);
    }

    @Test
    void crearViaje_debeRetornar201() throws Exception {
        Mockito.when(viajeService.crearViaje(any(ViajeDTO.class))).thenReturn(viaje);

        mockMvc.perform(post("/api/viajes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(viajeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(viaje.getId()));
    }

    @Test
    void listarViajes_debeRetornar200() throws Exception {
        Mockito.when(viajeService.listarViajes()).thenReturn(List.of(viaje));

        mockMvc.perform(get("/api/viajes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Viajes obtenidos con éxito"))
                .andExpect(jsonPath("$.data[0].empresaId").value(empresa.getId().intValue()));  // Usar empresa declarada
    }

    @Test
    void actualizarViaje_debeRetornar200() throws Exception {
        Mockito.when(viajeService.editarViaje(eq(1L), any(ViajeDTO.class))).thenReturn(viaje);

        mockMvc.perform(put("/api/viajes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(viajeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Viaje actualizado con éxito"))
                .andExpect(jsonPath("$.data.id").value(viaje.getId()));
    }

    @Test
    void eliminarViaje_debeRetornar200() throws Exception {
        Mockito.doNothing().when(viajeService).eliminarViaje(1L);

        mockMvc.perform(delete("/api/viajes/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Viaje eliminado con éxito"));
    }
}