package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.TipoServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.TipoServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.repository.TipoServicoRepository;

@Service
public class TipoServicoService {
	@Autowired
	TipoServicoRepository tipoServicoRepository;
	
	public List<TipoServicoResDTO> listar(Oficina oficina){
		return tipoServicoRepository.findByOficina(oficina).stream()
				.map(TipoServicoResDTO::new)
				.collect(Collectors.toList());
	}
	
	public TipoServicoResDTO criar(TipoServicoReqDTO dto, Oficina oficina) {
		TipoServico tipo = new TipoServico();
		tipo.setDescricao(dto.getDescricao());
		tipo.setOficina(oficina);
		TipoServico criado = tipoServicoRepository.save(tipo);
		return new TipoServicoResDTO(criado);
	}
}
