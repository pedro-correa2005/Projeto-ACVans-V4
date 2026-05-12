package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.StatusServicoDTO;
import com.fooengineers.projetoAcVansV4.service.StatusServicoService;

@RestController
@RequestMapping("/api/status-servico")
public class StatusServicoController {
	@Autowired
	StatusServicoService statusServicoService;
	
	@GetMapping
	public List<StatusServicoDTO> listar(){
		return statusServicoService.listar();
	}
}
