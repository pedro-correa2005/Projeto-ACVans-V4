package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.auditoria.dto.AuditoriaDTO;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Usuario;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {
	@Autowired
	AuditoriaService auditoriaService;
	
	@GetMapping
	public Page<AuditoriaDTO> auditar(Authentication authentication, Pageable pageable){
		Oficina oficina = ((Usuario) authentication.getPrincipal()).getOficina();
		return auditoriaService.consultar(oficina, pageable);
	}
}
