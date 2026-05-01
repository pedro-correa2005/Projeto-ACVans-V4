package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
	//Pesquisar por token de atualização
	Optional<Servico> findByTokenAtualizacao(String tokenAtualizacao);
	//Pesquisar por token de consulta
	Optional<Servico> findByTokenConsulta(String tokenConsulta);
	
	//Contar serviços por veículo
	int countByVeiculoPlacaAndOficina(String placa, Oficina oficina);
	//Filtrar por oficina
	Page<Servico> findByOficina(Oficina oficina, Pageable pageable);
}
