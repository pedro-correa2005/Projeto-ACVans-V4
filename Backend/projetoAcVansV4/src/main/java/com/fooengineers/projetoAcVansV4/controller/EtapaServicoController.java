package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.auditoria.annotation.Auditavel;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.dto.EtapaServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.EtapaServicoResDTO;
import com.fooengineers.projetoAcVansV4.dto.OrdemDTO;
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
	@Auditavel(acao = Acao.CREATE, entidade = Entidade.ETAPA_SERVICO)
	public ResponseEntity<EtapaServicoResDTO> criar(@ModelAttribute @Valid EtapaServicoReqDTO dto, @PathVariable(required=true) Long idTipoServico) {
		EtapaServicoResDTO criado = etapaServicoService.criar(dto, idTipoServico);
		return ResponseEntity.ok().body(criado);
	}
	
	@PutMapping("/etapas-servico/{idEtapaServico}")
	public ResponseEntity<EtapaServicoResDTO >atualizar(@ModelAttribute @Valid EtapaServicoReqDTO dto, @PathVariable(required=true) Long idEtapaServico) {
		EtapaServicoResDTO atualizado = etapaServicoService.atualizar(dto, idEtapaServico);
		return ResponseEntity.ok().body(atualizado);
	}
	
	@PatchMapping("/etapas-servico/{idEtapaServico}/atualizar-ordem")
	public ResponseEntity<EtapaServicoResDTO> atualizarOrdem(@ModelAttribute @Valid OrdemDTO dto, @PathVariable Long idEtapaServico){
		EtapaServicoResDTO atualizado = etapaServicoService.atualizarOrdem(dto, idEtapaServico);
		return ResponseEntity.ok().body(atualizado);
	}
	
	@DeleteMapping("/etapas-servico/{idEtapaServico}")
	@Auditavel(acao = Acao.DELETE, entidade = Entidade.ETAPA_SERVICO)
	public ResponseEntity<?> deletar(@PathVariable(required=true) Long idEtapaServico){
		etapaServicoService.deletar(idEtapaServico);
		return ResponseEntity.ok().build();
	}
}