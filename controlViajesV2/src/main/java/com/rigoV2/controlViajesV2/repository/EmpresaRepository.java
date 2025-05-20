package com.rigoV2.controlViajesV2.repository;

import com.rigoV2.controlViajesV2.entity.Empresa;
import com.rigoV2.controlViajesV2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
    List<Empresa> findByUsuario(Usuario nombre);
}
