package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.OficinaReqDTO;
import com.fooengineers.projetoAcVansV4.dto.OficinaResDTO;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.repository.OficinaRepository;
import com.fooengineers.projetoAcVansV4.specification.OficinaSpecification;

@Service
public class OficinaService {
	@Autowired
	OficinaRepository oficinaRepository;
	
	public List<OficinaResDTO> listar(String param){
		return oficinaRepository.findAll(OficinaSpecification.filtroGeral(param)).stream()
				.map(OficinaResDTO::new)
				.collect(Collectors.toList());
		
	}
	
	public OficinaResDTO criar(OficinaReqDTO dto) {
		Oficina oficina = new Oficina();
		oficina.setNome(dto.getNome());
		oficina.setAtivo(dto.isAtivo());
		Oficina criado = oficinaRepository.save(oficina);
		return new OficinaResDTO(criado);
	}
}
