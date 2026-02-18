package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.DireccionDTO;
import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.entity.Direccion;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.FacturaMapper;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de ServicioService para manejar la lógica de negocio
 * de los servicios.
 */
@Service
public class ServicioServiceImpl implements ServicioService {

    private static final Logger logger = LoggerFactory.getLogger(ServicioServiceImpl.class);

    @Autowired
    private ServicioMapper servicioMapper;
    @Autowired
    private  ServicioRepository servicioRepository;
    @Autowired
    private  FacturaRepository facturaRepository;
    @Autowired
    private  EmpresaRepository empresaRepository;






    @Override
    public ServicioDTO crearServicio(ServicioDTO servicioDTO) {
        logger.info("Creando un nuevo servicio tipo: {}", servicioDTO.getTipoServicio());


        Empresa empresa = empresaRepository.findById(servicioDTO.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empresa no encontrada con ID: " + servicioDTO.getEmpresaId()));


        Servicio servicio = servicioMapper.toEntity(servicioDTO, empresa);


        Servicio guardado = servicioRepository.save(servicio);


        return servicioMapper.toDTO(guardado);
    }

    @Override
    public List<ServicioDTO> listarServicios() {
        logger.info("Listando todos los servicios");
        return servicioRepository.findAll().stream()
                .map(servicioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioDTO obtenerServicioPorId(Long id) {
        logger.info("Buscando servicio con ID: {}", id);
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
        return servicioMapper.toDTO(servicio);
    }

    @Override
    public ServicioDTO actualizarServicio(Long id, ServicioDTO servicioDTO) {
        logger.info("Actualizando servicio con ID: {}", id);

        Servicio existente = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        // Actualizamos la factura si cambia
        // 1. Obtener el ID de Factura del DTO (puede ser nulo)
        Long dtoFacturaId = servicioDTO.getFacturaId();

        // 2. Obtener el ID de Factura de la entidad existente
        Long dbFacturaId = null;
        if (existente.getFactura() != null) {
            dbFacturaId = existente.getFactura().getId();
        }

        // 3. Compáramos de forma segura (Objects.equals maneja valores nulos)
        if (!Objects.equals(dtoFacturaId, dbFacturaId)) {
            logger.info("El ID de la factura ha cambiado. Actualizando...");

            // 4.a: Si el ID de la factura que viene del DTO (frontend) es nulo, entonces ponemos la factura de la entidad a nulo.
            if (dtoFacturaId == null) {
                existente.setFactura(null);
            } else {
                //Si el ID de la factura que viene del DTO (frontend) NO es nulo, entonces buscamos esa nueva factura y asígnala.
                Factura nuevaFactura = facturaRepository.findById(dtoFacturaId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Factura no encontrada con ID: " + dtoFacturaId));
                existente.setFactura(nuevaFactura);
            }
        }

        // Actualizamos todos los campos
        existente.setTipoServicio(servicioDTO.getTipoServicio());
        existente.setFechaServicio(servicioDTO.getFechaServicio());
        //existente.setOrigen(servicioDTO.getOrigen());
        //existente.setDestino(servicioDTO.getDestino());
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

        if (servicioDTO.getOrigen() != null) {
            existente.setOrigen(servicioDTO.getOrigen());
        }

        if (servicioDTO.getDestino() != null) {
            existente.setDestino(servicioDTO.getDestino());
        }

        Servicio actualizado = servicioRepository.save(existente);
        return servicioMapper.toDTO(actualizado);
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
                .map(servicioMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar para convertir DireccionDTO a la entidad Direccion (Embeddable)
     * Esto evita repetir código para Origen y Destino.
     */
   /* private Direccion mapToDireccion(DireccionDTO dto) {
        if (dto == null) return null;

        return Direccion.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .codigoPostal(dto.getCodigoPostal())
                .ciudad(dto.getCiudad())
                .provincia(dto.getProvincia())
                .pais(dto.getPais())
                .build();
    }

    */
}