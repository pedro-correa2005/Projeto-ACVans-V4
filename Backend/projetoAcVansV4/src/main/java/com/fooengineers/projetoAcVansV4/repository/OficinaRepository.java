package com.fooengineers.projetoAcVansV4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface OficinaRepository extends JpaRepository<Oficina, Integer>, JpaSpecificationExecutor<Oficina>{
	Page<Oficina> findAll(Specification<Oficina> specification, Pageable pageable);
}
