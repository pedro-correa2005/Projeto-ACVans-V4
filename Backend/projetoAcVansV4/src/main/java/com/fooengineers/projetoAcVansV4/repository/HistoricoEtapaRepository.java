package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fooengineers.projetoAcVansV4.entity.HistoricoEtapa;

public interface HistoricoEtapaRepository extends JpaRepository<HistoricoEtapa, Long>, JpaSpecificationExecutor<HistoricoEtapa> {	
	List<HistoricoEtapa> findAll(Specification<HistoricoEtapa> specification);
}
