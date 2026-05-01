package com.fooengineers.projetoAcVansV4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;

public interface EtapaServicoRepository extends JpaRepository<EtapaServico, Long> {
	//Pesquisa por tipo de servico e ordem (filtrado por tenant)
	Optional<EtapaServico> findByTipoServicoAndOrdemAndOficina(
		TipoServico tipoServico,
		Integer ordem,
		Oficina oficina
	);
	
	Optional<EtapaServico> findFirstByTipoServicoOrderByOrdemAsc(TipoServico tipoServico);
	Optional<EtapaServico> findFirstByTipoServicoAndOrdemLessThanOrderByOrdemDesc(TipoServico tipoServico,Integer ordem);
	Optional<EtapaServico> findFirstByTipoServicoAndOrdemGreaterThanOrderByOrdemAsc(TipoServico tipoServico, Integer ordem);
	
}
