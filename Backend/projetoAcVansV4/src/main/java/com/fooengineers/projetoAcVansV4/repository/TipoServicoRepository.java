package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;

public interface TipoServicoRepository extends JpaRepository<TipoServico, Long> {
	//Filtragem de tenant
	List<TipoServico> findByOficina(Oficina oficina);
	//Pesquisar por id (filtrada por tenant)
	Optional<TipoServico> findByIdAndOficina(Long id, Oficina oficina);
}
