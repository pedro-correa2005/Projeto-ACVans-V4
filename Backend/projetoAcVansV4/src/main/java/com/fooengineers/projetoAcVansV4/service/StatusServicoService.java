package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.StatusServicoDTO;
import com.fooengineers.projetoAcVansV4.repository.StatusServicoRepository;

@Service
public class StatusServicoService {
	@Autowired
	StatusServicoRepository statusServicoRepository;
	
	public List<StatusServicoDTO> listar(){
		return statusServicoRepository.findAll().stream()
				.map(StatusServicoDTO::new)
				.collect(Collectors.toList());
	}
}
