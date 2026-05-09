package com.fooengineers.projetoAcVansV4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.ConsultaResDTO;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;

@Service
public class ConsultaService {
	@Autowired
	EtapaServicoService etapaServicoService;
	@Autowired
	ServicoService servicoService;
	
	public ConsultaResDTO consultar(String token, String placa) {
		Servico servico = servicoService.buscar(token, placa);
		TipoServico tipo = servico.getTipoServico();
		
		ConsultaResDTO dto = new ConsultaResDTO();
		dto.setTipoServicoDescricao(tipo.getDescricao());
		dto.setEtapas(etapaServicoService.listarPorTipo(tipo));
		return dto;
	}
	
}
