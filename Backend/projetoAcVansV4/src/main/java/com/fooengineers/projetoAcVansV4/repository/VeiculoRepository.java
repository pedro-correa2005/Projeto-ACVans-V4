package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
	//Query para filtrar por cliente
	Page<Veiculo> findByCliente(Cliente c ,Pageable pageable);
	
	//Query para filtrar por placa e oficina
	Optional<Veiculo> findByPlacaAndOficina(String placa, Oficina oficina);
	
	//Query para contar número de veículos por cliente
	long countByCliente(Cliente c);
}
