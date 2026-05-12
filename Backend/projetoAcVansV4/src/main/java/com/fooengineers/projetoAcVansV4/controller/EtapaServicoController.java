package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.EtapaServicoResDTO;
import com.fooengineers.projetoAcVansV4.service.EtapaServicoService;

@RestController
@RequestMapping("/api/tipos-servico")
public class EtapaServicoController {
	@Autowired
	EtapaServicoService etapaServicoService;
	
	@GetMapping("/{idTipoServico}/etapas-servico")
	public List<EtapaServicoResDTO> listar(@PathVariable(required=true) Long idTipoServico){
		return etapaServicoService.listarPorTipo(idTipoServico);
	}
}
