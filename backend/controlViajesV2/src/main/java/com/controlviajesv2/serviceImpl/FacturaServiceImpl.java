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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class FacturaServiceImpl implements FacturaService {

    private static final Logger logger = LoggerFactory.getLogger(FacturaServiceImpl.class);

    @Autowired
    private  EmpresaRepository empresaRepository;
    @Autowired
    private  UsuarioRepository usuarioRepository;
    @Autowired
    private  ServicioRepository servicioRepository;

    @Autowired
    private FacturaMapper facturaMapper;
    @Autowired
    private FacturaRepository facturaRepository;


    @Override
    @Transactional
    public FacturaDTO crearFactura(FacturaDTO facturaDTO) {
        logger.info("Creando nueva factura número: {}", facturaDTO.getNumeroFactura());

        Empresa empresa = empresaRepository.findById(facturaDTO.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + facturaDTO.getEmpresaId()));

        Usuario usuario = usuarioRepository.findById(facturaDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + facturaDTO.getUsuarioId()));

        //generamos aqui el numero de factura e ignoramos el que traiga dedesde el fronted
        String nuevoNumero = generarSiguienteNumeroFactura(empresa, usuario);

        facturaDTO.setNumeroFactura(nuevoNumero);
        logger.info("Generado número de factura único: {}", nuevoNumero);

        Factura factura = facturaMapper.toEntity(facturaDTO, empresa, usuario);
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


        return facturaMapper.toDTO(guardada);
    }

    @Override
    public List<FacturaDTO> listarFacturas() {
        logger.info("Listando todas las facturas");
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FacturaDTO obtenerFacturaPorId(Long id) {
        logger.info("Obteniendo factura con ID: {}", id);
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con ID: " + id));
        return facturaMapper.toDTO(factura);
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
        return facturaMapper.toDTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminarFactura(Long id) {
        logger.info("Eliminando factura con ID: {}", id);
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con ID: " + id));


        if (factura.getServicios() != null && !factura.getServicios().isEmpty()) {
            List<Servicio> serviciosACopiar = new ArrayList<>(factura.getServicios());//creamos una copia para evitar ConcurrentModificationException
            for (Servicio servicio :serviciosACopiar) {
                servicio.setFactura(null); // El servicio queda "libre"
            }

            factura.getServicios().clear();//limpiamos la lista aoriginal


            // Guardamos los cambios en los servicios (ahora huérfanos de factura, pero vivos)
            servicioRepository.saveAll(serviciosACopiar);
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
                .map(facturaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void validarDatosFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada"));

        // 1. Validar Cliente (Usuario)
        if (factura.getUsuario() == null || factura.getUsuario().getDireccion() == null) {
            throw new IllegalArgumentException("No se puede generar la factura: El cliente " +
                    (factura.getUsuario() != null ? factura.getUsuario().getNombre() : "desconocido") +
                    " no tiene dirección configurada en su perfil.");
        }

        // 2. Validar Proveedor (Empresa)
        if (factura.getEmpresa() == null || factura.getEmpresa().getDireccion() == null) {
            throw new IllegalArgumentException("No se puede generar la factura: La empresa " +
                    (factura.getEmpresa() != null ? factura.getEmpresa().getNombre() : "desconocida") +
                    " no tiene dirección fiscal configurada.");
        }
    }


    // Método auxiliar actualizado para incluir ID de Usuario
    private String generarSiguienteNumeroFactura(Empresa empresa, Usuario usuario) {
        int year = java.time.LocalDate.now().getYear();

        // 1. Definimos el prefijo único por usuario (Ej: "U5-")
        String prefijoUsuario = "U" + usuario.getId() + "-";

        // 2. Buscamos todas las facturas de la empresa
        List<Factura> facturas = facturaRepository.findByEmpresaId(empresa.getId());

        // 3. Filtramos: mismo año Y que empiecen por el prefijo de este usuario
        // Esto es importante para no contar facturas antiguas con formato viejo
        int maxSecuencia = facturas.stream()
                .filter(f -> f.getFechaEmision().getYear() == year)
                .filter(f -> f.getNumeroFactura().startsWith(prefijoUsuario))
                .mapToInt(f -> {
                    try {
                        // Formato: U1-N-005/2026
                        // Queremos sacar el "005".
                        // Split por "N-" nos da ["U1-", "005/2026"]
                        // Cogemos la segunda parte, y split por "/" -> "005"
                        String[] partes = f.getNumeroFactura().split("-N-");
                        String numeroYAnio = partes[1]; // "005/2026"
                        String numeroPuro = numeroYAnio.split("/")[0]; // "005"

                        return Integer.parseInt(numeroPuro);
                    } catch (Exception e) {
                        return 0; // Si el formato es raro, lo ignoramos
                    }
                })
                .max()
                .orElse(0);

        // 4. Generamos el nuevo número: U1-N-001/2026
        return String.format("%sN-%03d/%d", prefijoUsuario, maxSecuencia + 1, year);
    }
}