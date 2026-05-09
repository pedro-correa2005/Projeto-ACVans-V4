package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.HistoricoEtapa;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Servico;

public interface HistoricoEtapaRepository extends JpaRepository<HistoricoEtapa, Long> {
	//Filtrar por servico, etapa e tenant
	Optional<HistoricoEtapa> findByServicoAndEtapaServicoAndOficina(
			Servico servico,
			EtapaServico etapaServico,
			Oficina oficina
	);
	List<HistoricoEtapa> findByOficina(Oficina oficina);
}
