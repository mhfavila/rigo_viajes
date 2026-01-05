package com.controlviajesv2.serviceImpl;

import com.controlviajesv2.dto.FacturaDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.exception.ResourceNotFoundException;
import com.controlviajesv2.mapper.FacturaMapper;
import com.controlviajesv2.repository.EmpresaRepository;
import com.controlviajesv2.repository.FacturaRepository;
import com.controlviajesv2.repository.ServicioRepository;
import com.controlviajesv2.repository.UsuarioRepository;
import com.controlviajesv2.service.FacturaService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class FacturaServiceImpl implements FacturaService {

    private static final Logger logger = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;


    public FacturaServiceImpl(FacturaRepository facturaRepository,
                              EmpresaRepository empresaRepository,
                              UsuarioRepository usuarioRepository, ServicioRepository servicioRepository) {
        this.facturaRepository = facturaRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;

        this.servicioRepository = servicioRepository;
    }

    @Override
    @Transactional
    public FacturaDTO crearFactura(FacturaDTO facturaDTO) {
        logger.info("Creando nueva factura número: {}", facturaDTO.getNumeroFactura());

        Empresa empresa = empresaRepository.findById(facturaDTO.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + facturaDTO.getEmpresaId()));

        Usuario usuario = usuarioRepository.findById(facturaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + facturaDTO.getUsuarioId()));

        Factura factura = FacturaMapper.toEntity(facturaDTO, empresa, usuario);
        Factura guardada = facturaRepository.save(factura);

        if (facturaDTO.getServiciosIds() != null && !facturaDTO.getServiciosIds().isEmpty()) {

            logger.info("Asociando {} servicios a la factura {}", facturaDTO.getServiciosIds().size(), guardada.getNumeroFactura());

            // 1. Buscar las entidades Servicio
            List<Servicio> serviciosParaAsociar = servicioRepository.findAllById(facturaDTO.getServiciosIds());

            // 2. Iterar y actualizar
            for (Servicio servicio : serviciosParaAsociar) {
                //servicio.setFacturado(true);      // Marcar como facturado
                servicio.setFactura(guardada);  // Asignar esta nueva factura
            }

            // 3. Guardar los servicios actualizados
            servicioRepository.saveAll(serviciosParaAsociar);

            // 4. (Opcional) Asignar al DTO de retorno
            guardada.setServicios(serviciosParaAsociar);
        }


        return FacturaMapper.toDTO(guardada);
    }

    @Override
    public List<FacturaDTO> listarFacturas() {
        logger.info("Listando todas las facturas");
        return facturaRepository.findAll().stream()
                .map(FacturaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FacturaDTO obtenerFacturaPorId(Long id) {
        logger.info("Obteniendo factura con ID: {}", id);
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con ID: " + id));
        return FacturaMapper.toDTO(factura);
    }

    @Override
    public FacturaDTO actualizarFactura(Long id, FacturaDTO facturaDTO) {
        logger.info("Actualizando factura con ID: {}", id);

        Factura existente = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con ID: " + id));

        // Actualizar datos básicos
        existente.setNumeroFactura(facturaDTO.getNumeroFactura());
        existente.setFechaEmision(facturaDTO.getFechaEmision());
        existente.setTotalBruto(facturaDTO.getTotalBruto());
        existente.setPorcentajeIva(facturaDTO.getPorcentajeIva());
        existente.setImporteIva(facturaDTO.getImporteIva());
        existente.setPorcentajeIrpf(facturaDTO.getPorcentajeIrpf());
        existente.setImporteIrpf(facturaDTO.getImporteIrpf());
        existente.setTotalFactura(facturaDTO.getTotalFactura());
        existente.setCuentaBancaria(facturaDTO.getCuentaBancaria());
        existente.setFormaPago(facturaDTO.getFormaPago());
        existente.setObservaciones(facturaDTO.getObservaciones());
        existente.setEstado(facturaDTO.getEstado());

        // Si se cambia empresa o usuario
        if (!facturaDTO.getEmpresaId().equals(existente.getEmpresa().getId())) {
            Empresa nuevaEmpresa = empresaRepository.findById(facturaDTO.getEmpresaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + facturaDTO.getEmpresaId()));
            existente.setEmpresa(nuevaEmpresa);
        }

        if (!facturaDTO.getUsuarioId().equals(existente.getUsuario().getId())) {
            Usuario nuevoUsuario = usuarioRepository.findById(facturaDTO.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + facturaDTO.getUsuarioId()));
            existente.setUsuario(nuevoUsuario);
        }

        Factura actualizada = facturaRepository.save(existente);
        return FacturaMapper.toDTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminarFactura(Long id) {
        logger.info("Eliminando factura con ID: {}", id);
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con ID: " + id));

        // 1. DESVINCULAR LOS SERVICIOS
        // Recorremos los servicios de esta factura y les quitamos la referencia
        if (factura.getServicios() != null) {
            for (Servicio servicio : factura.getServicios()) {
                servicio.setFactura(null); // El servicio queda "libre"
            }
            // Guardamos los cambios en los servicios (ahora huérfanos de factura, pero vivos)
            servicioRepository.saveAll(factura.getServicios());
        }


        // Ahora que está vacía de servicios, la borramos tranquilamente
        facturaRepository.delete(factura);
    }


    /**
     * busca las facturas de una empresa
     * @param empresaId
     * @return
     */
    @Override
    public List<FacturaDTO> getFacturasPorEmpresa(Long empresaId) {

        List<Factura> facturas = facturaRepository.findByEmpresaId(empresaId);
        return facturas.stream()
                .map(FacturaMapper::toDTO)
                .collect(Collectors.toList());
    }
}