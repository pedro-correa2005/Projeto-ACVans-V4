package com.fooengineers.projetoAcVansV4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	Page<Cliente> findByOficina(Oficina oficina, Pageable pageable);
}
