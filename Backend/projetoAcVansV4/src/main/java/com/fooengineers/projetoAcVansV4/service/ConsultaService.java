package com.fooengineers.projetoAcVansV4.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.ConsultaResDTO;
import com.fooengineers.projetoAcVansV4.dto.EtapaServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.exception.ServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.EtapaServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.ServicoRepository;
import com.fooengineers.projetoAcVansV4.specification.ServicoSpecification;

@Service
public class ConsultaService {
	@Autowired
	ServicoRepository servicoRepository;
	@Autowired
	EtapaServicoRepository etapaServicoRepository;
	
	public ConsultaResDTO consultar(String token, String placa) {
		Servico servico = servicoRepository.findOne(ServicoSpecification.filtroDeConsulta(token, placa)).orElseThrow(() -> new ServicoNaoEncontradoException());
		TipoServico tipo = servico.getTipoServico();
		ConsultaResDTO dto = new ConsultaResDTO();
		dto.setTipoServicoDescricao(tipo.getDescricao());
		dto.setEtapas(etapaServicoRepository.findByTipoServico(tipo).stream().map(EtapaServicoResDTO::new).collect(Collectors.toList()));
		dto.setIdEtapaServico(servico.getEtapaServico().getId());
		return dto;
	}
	public ConsultaResDTO consultar (String tokenLongo) {
		Servico servico = servicoRepository.findByTokenAtualizacao(tokenLongo).orElseThrow(() -> new ServicoNaoEncontradoException());
		TipoServico tipo = servico.getTipoServico();
		ConsultaResDTO dto = new ConsultaResDTO();
		dto.setTipoServicoDescricao(tipo.getDescricao());
		dto.setEtapas(etapaServicoRepository.findByTipoServico(tipo).stream().map(EtapaServicoResDTO::new).collect(Collectors.toList()));
		dto.setIdEtapaServico(servico.getEtapaServico().getId());
		return dto;
	}
}
