package com.rigoV2.controlViajesV2.repository;

import com.rigoV2.controlViajesV2.entity.Empresa;
import com.rigoV2.controlViajesV2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
    List<Empresa> findByUsuario(Usuario nombre);
}
