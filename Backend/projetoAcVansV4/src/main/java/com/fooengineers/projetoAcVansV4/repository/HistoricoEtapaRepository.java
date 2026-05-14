package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.HistoricoEtapa;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Servico;

public interface HistoricoEtapaRepository extends JpaRepository<HistoricoEtapa, Long>, JpaSpecificationExecutor<HistoricoEtapa> {
	//Filtrar por servico, etapa e tenant
	Optional<HistoricoEtapa> findByServicoAndEtapaServicoAndOficina(
			Servico servico,
			EtapaServico etapaServico,
			Oficina oficina
	);
	List<HistoricoEtapa> findByOficina(Oficina oficina);
	
	List<HistoricoEtapa> findAll(Specification<HistoricoEtapa> specification);
}
