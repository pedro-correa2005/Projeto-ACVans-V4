package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.OficinaResDTO;
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
}
