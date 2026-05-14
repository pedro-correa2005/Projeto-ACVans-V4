package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long>, JpaSpecificationExecutor<Servico>{
	//Pesquisar por token de atualização
	Optional<Servico> findByTokenAtualizacao(String tokenAtualizacao);
	//Pesquisar por token de consulta
	Optional<Servico> findOne(Specification<Servico> specification);
	
	//Contar serviços por veículo
	int countByVeiculoPlacaAndOficina(String placa, Oficina oficina);
	//Filtrar por oficina e termo
	Page<Servico> findAll(Specification<Servico> specification, Pageable pageable);
	
	Page<Servico> findByOficina(Oficina oficina, Pageable pageable);
}
