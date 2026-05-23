package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.TipoServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.TipoServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.TipoServicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tipos-servico")
public class TipoServicoController {
	@Autowired
	TipoServicoService tipoServicoService;
	
	@GetMapping
	public List<TipoServicoResDTO> listar(Authentication authentication){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return tipoServicoService.listar(usuario.getOficina());
	}
	
	@PostMapping
	public ResponseEntity<TipoServicoResDTO> criar(@RequestBody @Valid TipoServicoReqDTO dto, Authentication authentication) {
		Usuario usuario = (Usuario) authentication.getPrincipal();
		TipoServicoResDTO criado = tipoServicoService.criar(dto, usuario.getOficina());
		return ResponseEntity.ok().body(criado);
	}
	
	@PutMapping("/{idTipoServico}")
	public ResponseEntity<TipoServicoResDTO> atualizar(@RequestBody @Valid TipoServicoReqDTO dto, @PathVariable(required=true) Long idTipoServico) {
		TipoServicoResDTO atualizado = tipoServicoService.atualizar(dto, idTipoServico);
		return ResponseEntity.ok().body(atualizado);
	}
	
	@DeleteMapping("/{idTipoServico}")
	public ResponseEntity<?> deletar(@PathVariable(required=true) Long idTipoServico){
		tipoServicoService.deletar(idTipoServico);
		return ResponseEntity.ok().build();
	}
}
