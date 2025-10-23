package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.ServicioMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.FacturaRepository;
import com.controlviajesv2.repository.ServicioRepository;
import com.controlviajesv2.service.ServicioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de ServicioService para manejar la lógica de negocio
 * de los servicios.
 */
@Service
public class ServicioServiceImpl implements ServicioService {

    private static final Logger logger = LoggerFactory.getLogger(ServicioServiceImpl.class);

    private final ServicioRepository servicioRepository;
    private final FacturaRepository facturaRepository;
    private final EmpresaRepository empresaRepository;


    public ServicioServiceImpl(ServicioRepository servicioRepository, FacturaRepository facturaRepository, EmpresaRepository empresaRepository) {
        this.servicioRepository = servicioRepository;
        this.facturaRepository = facturaRepository;
        this.empresaRepository = empresaRepository;
    }

    @Override
    public ServicioDTO crearServicio(ServicioDTO servicioDTO) {
        logger.info("Creando un nuevo servicio tipo: {}", servicioDTO.getTipoServicio());


        Empresa empresa = empresaRepository.findById(servicioDTO.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empresa no encontrada con ID: " + servicioDTO.getEmpresaId()));

        // ⚙️ Convertir DTO a entidad (sin factura, pero con empresa)
        Servicio servicio = ServicioMapper.toEntity(servicioDTO, empresa);

        // 💾 Guardar el servicio
        Servicio guardado = servicioRepository.save(servicio);

        // 🔁 Devolver el DTO
        return ServicioMapper.toDTO(guardado);
    }

    @Override
    public List<ServicioDTO> listarServicios() {
        logger.info("Listando todos los servicios");
        return servicioRepository.findAll().stream()
                .map(ServicioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioDTO obtenerServicioPorId(Long id) {
        logger.info("Buscando servicio con ID: {}", id);
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
        return ServicioMapper.toDTO(servicio);
    }

    @Override
    public ServicioDTO actualizarServicio(Long id, ServicioDTO servicioDTO) {
        logger.info("Actualizando servicio con ID: {}", id);

        Servicio existente = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        // Actualizamos la factura si cambia
        if (!servicioDTO.getFacturaId().equals(existente.getFactura().getId())) {
            Factura nuevaFactura = facturaRepository.findById(servicioDTO.getFacturaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Factura no encontrada con ID: " + servicioDTO.getFacturaId()));
            existente.setFactura(nuevaFactura);
        }

        // Actualizamos todos los campos
        existente.setTipoServicio(servicioDTO.getTipoServicio());
        existente.setFechaServicio(servicioDTO.getFechaServicio());
        existente.setOrigen(servicioDTO.getOrigen());
        existente.setDestino(servicioDTO.getDestino());
        existente.setConductor(servicioDTO.getConductor());
        existente.setMatriculaVehiculo(servicioDTO.getMatriculaVehiculo());
        existente.setKm(servicioDTO.getKm());
        existente.setPrecioKm(servicioDTO.getPrecioKm());
        existente.setImporteServicio(servicioDTO.getImporteServicio());
        existente.setDieta(servicioDTO.getDieta());
        existente.setPrecioDieta(servicioDTO.getPrecioDieta());
        existente.setHorasEspera(servicioDTO.getHorasEspera());
        existente.setImporteEspera(servicioDTO.getImporteEspera());
        existente.setAlbaran(servicioDTO.getAlbaran());
        existente.setClienteFinal(servicioDTO.getClienteFinal());
        existente.setObservaciones(servicioDTO.getObservaciones());
        existente.setOrden(servicioDTO.getOrden());

        Servicio actualizado = servicioRepository.save(existente);
        return ServicioMapper.toDTO(actualizado);
    }

    @Override
    public void eliminarServicio(Long id) {
        logger.info("Eliminando servicio con ID: {}", id);
        if (!servicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Servicio no encontrado con ID: " + id);
        }
        servicioRepository.deleteById(id);
    }

    /**
     * busca los serviciso asociados a una empresa
     * @param empresaId
     * @return
     */
    @Override
    public List<ServicioDTO> findByEmpresaId(Long empresaId) {
        return servicioRepository.findByEmpresaId(empresaId)
                .stream()
                .map(ServicioMapper::toDTO)
                .collect(Collectors.toList());
    }
}