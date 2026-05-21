package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.StatusServico;

public interface StatusServicoRepository extends JpaRepository<StatusServico, Integer> {
	Optional<StatusServico> findByDescricao(String descricao);
}
