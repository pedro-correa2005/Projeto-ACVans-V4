package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Oficina;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	//Filtro de tenant
	Optional<Cliente> findByIdClienteAndOficina(Long idCliente, Oficina oficina);
}
