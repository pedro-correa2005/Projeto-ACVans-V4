package com.fooengineers.projetoAcVansV4.repository;

import java.util.List;
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
	
	//Encontra a etapa de menor ordem (primeira)
	Optional<EtapaServico> findFirstByTipoServicoOrderByOrdemAsc(TipoServico tipoServico);
	//Encontra a etapa cuja ordem corresponde à ordem anterior 
	Optional<EtapaServico> findFirstByTipoServicoAndOrdemLessThanOrderByOrdemDesc(TipoServico tipoServico,Integer ordem);
	//Encontra a etapa cuja ordem corresponde à próxima ordem
	Optional<EtapaServico> findFirstByTipoServicoAndOrdemGreaterThanOrderByOrdemAsc(TipoServico tipoServico, Integer ordem);
	
	//Listagem por tipo de serviço (filtrado por tenant)
	List<EtapaServico> findByTipoServicoAndOficina(TipoServico tipoServico, Oficina oficina);
	
}
