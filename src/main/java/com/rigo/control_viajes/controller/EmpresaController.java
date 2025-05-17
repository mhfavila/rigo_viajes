package com.rigo.control_viajes.controller;

import com.rigo.control_viajes.DTO.EmpresaDTO;
import com.rigo.control_viajes.mapper.EmpresaMapper;
import com.rigo.control_viajes.model.Empresa;
import com.rigo.control_viajes.repository.EmpresaRepository;
import com.rigo.control_viajes.service.EmpresaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

    @PostMapping
    public ResponseEntity<?> crearEmpresa(@Valid @RequestBody EmpresaDTO empresaDTO) {
        Empresa empresa = new Empresa();
        empresa.setNombre(empresaDTO.getNombre());
        empresa.setCif(empresaDTO.getCif());
        empresa.setTelefono(empresaDTO.getTelefono());
        empresa.setEmail(empresaDTO.getEmail());
        logger.info("Se ha creado la empresa: nombre={}, cif={}, teléfono={}, email={}",
                empresa.getNombre(), empresa.getCif(), empresa.getTelefono(), empresa.getEmail());

        Empresa empresaCreada = empresaService.crearEmpresa(empresa);
        logger.info("Empresa creada: {}", empresa.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Empresa creada con éxito",
                "data", empresaCreada
        ));
    }

    @GetMapping
    public ResponseEntity<?> listarEmpresas() {
        List<Empresa> empresas = empresaService.listarEmpresas();
        List<EmpresaDTO> empresaDTOs = EmpresaMapper.toDTOList(empresas);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Empresas obtenidas con éxito",
                "data", empresaDTOs
        ));
    }

    @GetMapping("/{id}")
    public Empresa obtenerEmpresaConViajes(@PathVariable Long id) {
        return empresaService.obtenerEmpresaConViajes(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarEmpresa(@PathVariable Long id, @Valid @RequestBody EmpresaDTO empresaDTO) {
        Empresa empresaEditada = empresaService.editarEmpresa(id, empresaDTO);
        logger.info("empresa editada correctamente: {}", empresaEditada);
        return ResponseEntity.ok(Map.of("mensaje", "Empresa actualizada con éxito", "data", empresaEditada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEmpresa(@PathVariable Long id) {
        empresaService.eliminarEmpresa(id);
        logger.info("Empresa eliminada");
        return ResponseEntity.ok(Map.of("mensaje", "Empresa eliminada con éxito"));
    }
}
