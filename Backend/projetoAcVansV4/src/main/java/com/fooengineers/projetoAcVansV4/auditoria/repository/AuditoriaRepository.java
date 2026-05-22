package com.fooengineers.projetoAcVansV4.auditoria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Auditoria;
import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
	Page<Auditoria> findByOficina(Oficina oficina, Pageable pageable);
}
