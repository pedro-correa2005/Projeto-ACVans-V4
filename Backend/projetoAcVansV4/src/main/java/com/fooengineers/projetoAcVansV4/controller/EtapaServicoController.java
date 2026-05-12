package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.EtapaServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.EtapaServicoResDTO;
import com.fooengineers.projetoAcVansV4.service.EtapaServicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class EtapaServicoController {
	@Autowired
	EtapaServicoService etapaServicoService;
	
	@GetMapping("/tipos-servico/{idTipoServico}/etapas-servico")
	public List<EtapaServicoResDTO> listar(@PathVariable(required=true) Long idTipoServico){
		return etapaServicoService.listarPorTipo(idTipoServico);
	}
	
	@PostMapping("/tipos-servico/{idTipoServico}/etapas-servico")
	public EtapaServicoResDTO criar(@ModelAttribute @Valid EtapaServicoReqDTO dto, @PathVariable(required=true) Long idTipoServico) {
		return etapaServicoService.criar(dto, idTipoServico);
	}
	
	@PutMapping("/api/etapas-servico/{idEtapaServico}")
	public EtapaServicoResDTO atualizar(@ModelAttribute @Valid EtapaServicoReqDTO dto, @PathVariable(required=true) Long idEtapaServico) {
		return etapaServicoService.atualizar(dto, idEtapaServico);
	}
}
