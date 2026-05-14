package com.fooengineers.projetoAcVansV4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.AuditoriaDTO;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.repository.AuditoriaRepository;

@Service
public class AuditoriaService {
	@Autowired
	AuditoriaRepository auditoriaRepository;
	
	public Page<AuditoriaDTO> consultar(Oficina oficina, Pageable pageable) {
		return auditoriaRepository.findByOficinaOrderByTempoDesc(oficina, pageable).map(AuditoriaDTO::new);
	}
}
