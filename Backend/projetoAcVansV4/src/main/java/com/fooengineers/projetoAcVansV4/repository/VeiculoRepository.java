package com.fooengineers.projetoAcVansV4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
	
}
