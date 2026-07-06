package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.mapper.ServicioMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.FacturaRepository;
import com.controlviajesv2.repository.ServicioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioServiceImplTest {

    @Mock
    private ServicioMapper servicioMapper;
    @Mock
    private ServicioRepository servicioRepository;
    @Mock
    private FacturaRepository facturaRepository;
    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private ServicioServiceImpl servicioService;

    @Test
    void actualizarServicio_desasociaFacturaCuandoDtoTraeNull() {
        Factura factura = new Factura();
        ReflectionTestUtils.setField(factura, "id", 7L);

        Empresa empresa = new Empresa();
        ReflectionTestUtils.setField(empresa, "id", 2L);

        Servicio existente = new Servicio();
        ReflectionTestUtils.setField(existente, "id", 1L);
        ReflectionTestUtils.setField(existente, "empresa", empresa);
        ReflectionTestUtils.setField(existente, "factura", factura);

        ServicioDTO dto = servicioDtoBase();
        ReflectionTestUtils.setField(dto, "facturaId", null);

        Servicio guardado = new Servicio();
        ReflectionTestUtils.setField(guardado, "id", 1L);
        ReflectionTestUtils.setField(guardado, "empresa", empresa);
        ReflectionTestUtils.setField(guardado, "factura", null);

        ServicioDTO salida = servicioDtoBase();
        ReflectionTestUtils.setField(salida, "id", 1L);
        ReflectionTestUtils.setField(salida, "facturaId", null);

        when(servicioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(servicioRepository.save(any(Servicio.class))).thenReturn(guardado);
        when(servicioMapper.toDTO(guardado)).thenReturn(salida);

        ServicioDTO resultado = servicioService.actualizarServicio(1L, dto);

        assertThat(ReflectionTestUtils.getField(existente, "factura")).isNull();
        assertThat(resultado).isSameAs(salida);
        assertThat(ReflectionTestUtils.getField(resultado, "facturaId")).isNull();
        verify(servicioRepository).save(existente);
    }

    @Test
    void actualizarServicio_asociaNuevaFacturaCuandoIdCambia() {
        Factura facturaInicial = new Factura();
        ReflectionTestUtils.setField(facturaInicial, "id", 7L);

        Factura facturaNueva = new Factura();
        ReflectionTestUtils.setField(facturaNueva, "id", 9L);

        Servicio existente = mock(Servicio.class);
        when(existente.getFactura()).thenReturn(facturaInicial);

        ServicioDTO dto = servicioDtoBase();
        ReflectionTestUtils.setField(dto, "facturaId", 9L);

        Servicio guardado = new Servicio();
        ReflectionTestUtils.setField(guardado, "id", 1L);
        ReflectionTestUtils.setField(guardado, "factura", facturaNueva);

        ServicioDTO salida = servicioDtoBase();
        ReflectionTestUtils.setField(salida, "id", 1L);
        ReflectionTestUtils.setField(salida, "facturaId", 9L);

        when(servicioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(facturaRepository.findById(9L)).thenReturn(Optional.of(facturaNueva));
        when(servicioRepository.save(any(Servicio.class))).thenReturn(guardado);
        when(servicioMapper.toDTO(guardado)).thenReturn(salida);

        ServicioDTO resultado = servicioService.actualizarServicio(1L, dto);

        verify(existente).setFactura(facturaNueva);
        assertThat(resultado).isSameAs(salida);
        assertThat(ReflectionTestUtils.getField(resultado, "facturaId")).isEqualTo(9L);
        verify(facturaRepository).findById(9L);
        verify(servicioRepository).save(any(Servicio.class));
    }

    private ServicioDTO servicioDtoBase() {
        ServicioDTO dto = new ServicioDTO();
        ReflectionTestUtils.setField(dto, "empresaId", 2L);
        ReflectionTestUtils.setField(dto, "tipoServicio", "Traslado");
        ReflectionTestUtils.setField(dto, "fechaServicio", LocalDate.now());
        ReflectionTestUtils.setField(dto, "origen", "Madrid");
        ReflectionTestUtils.setField(dto, "destino", "Sevilla");
        ReflectionTestUtils.setField(dto, "conductor", "Juan");
        ReflectionTestUtils.setField(dto, "matriculaVehiculo", "1234ABC");
        ReflectionTestUtils.setField(dto, "km", 100);
        ReflectionTestUtils.setField(dto, "precioKm", new BigDecimal("1.50"));
        ReflectionTestUtils.setField(dto, "importeServicio", new BigDecimal("150.00"));
        ReflectionTestUtils.setField(dto, "dieta", false);
        ReflectionTestUtils.setField(dto, "precioDieta", BigDecimal.ZERO);
        ReflectionTestUtils.setField(dto, "horasEspera", BigDecimal.ZERO);
        ReflectionTestUtils.setField(dto, "importeEspera", BigDecimal.ZERO);
        ReflectionTestUtils.setField(dto, "albaran", "ALB-1");
        ReflectionTestUtils.setField(dto, "clienteFinal", "Cliente X");
        ReflectionTestUtils.setField(dto, "observaciones", "ok");
        ReflectionTestUtils.setField(dto, "orden", 1);
        return dto;
    }
}
