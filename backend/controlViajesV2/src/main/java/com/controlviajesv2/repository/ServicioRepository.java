package com.controlviajesv2.repository;

import com.controlviajesv2.dto.ServicioDTO;
import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // Buscar todos los servicios de una factura específica
    List<Servicio> findByFacturaId(Long facturaId);

    // Buscar servicios por tipo
    List<Servicio> findByTipoServicio(String tipoServicio);

    // buscar servicios de una factura ordenados por orden
    List<Servicio> findByFacturaIdOrderByOrdenAsc(Long facturaId);
    //buscar los servicios que noe stan asignadosa ninguna factura
    List<Servicio> findByFacturaIsNull();

    //busca los servicios de una empresa por su id
    List<Servicio> findByEmpresaId(Long empresaId);


    //Busca todos los servicios que pertenezcan a la empresa indicada y que no tengan factura asignada.
    List<Servicio> findByEmpresaAndFacturaIsNull(Empresa empresa);//esta sin usar todavia
}


