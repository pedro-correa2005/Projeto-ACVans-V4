package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Auditoria;
import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
	//Filtro de Tenant
	Optional<Auditoria> findByIdAndOficina(Long id, Oficina oficina);
}
