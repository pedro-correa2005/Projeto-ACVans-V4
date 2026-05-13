package com.fooengineers.projetoAcVansV4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente>{
	Page<Cliente> findByOficina(Oficina oficina, Pageable pageable);
	Page<Cliente> findByOficinaAndNomeContainingIgnoreCase(Oficina oficina, String nome, Pageable pageable);
}
