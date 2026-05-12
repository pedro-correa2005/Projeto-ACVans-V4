package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.EtapaServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.exception.TipoServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.EtapaServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.TipoServicoRepository;

@Service
public class EtapaServicoService {
	@Autowired
	private EtapaServicoRepository etapaServicoRepository;
	@Autowired
	private TipoServicoRepository tipoServicoRepository;
	
	public List<EtapaServicoResDTO> listarPorTipo(TipoServico tipo){
		return etapaServicoRepository.findByTipoServico(tipo)
				.stream().map(EtapaServicoResDTO::new)
				.collect(Collectors.toList());
	}
	public List<EtapaServicoResDTO> listarPorTipo(Long idTipoServico){
		TipoServico tipo = tipoServicoRepository.findById(idTipoServico).orElseThrow(() -> new TipoServicoNaoEncontradoException(idTipoServico));
		return etapaServicoRepository.findByTipoServico(tipo)
				.stream().map(EtapaServicoResDTO::new)
				.collect(Collectors.toList());
	}
}
