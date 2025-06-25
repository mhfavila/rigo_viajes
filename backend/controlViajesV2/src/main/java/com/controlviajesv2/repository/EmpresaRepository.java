package com.controlviajesv2.repository;

import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
    List<Empresa> findByUsuario(Usuario nombre);
}
