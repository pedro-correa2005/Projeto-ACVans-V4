package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.ServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.ServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.ServicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {
	@Autowired
	private ServicoService servicoService;
	@GetMapping
	public Page<ServicoResDTO> listar(Authentication authentication, Pageable pageable){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return servicoService.listar(usuario.getOficina(), pageable);
	}
	
	@PostMapping
	public ResponseEntity<ServicoResDTO> criar(@ModelAttribute @Valid ServicoReqDTO dto, Authentication authentication){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		ServicoResDTO criado = servicoService.criar(dto, usuario.getOficina().getId());
		return ResponseEntity.ok(criado);
	}
	
	@PutMapping("/{idServico}")
	public ResponseEntity<ServicoResDTO> criar(@ModelAttribute @Valid ServicoReqDTO dto, @PathVariable(required=true) Long idServico){
		ServicoResDTO atualizado = servicoService.atualizar(dto, idServico);
		return ResponseEntity.ok(atualizado);
	}
	
	@DeleteMapping("/{idServico}")
	public ResponseEntity<?> deletar(@PathVariable(required=true) Long idServico){
		servicoService.deletar(idServico);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/baixar-qrcode")
	public ResponseEntity<byte[]> baixarQrCode(@RequestParam(required=true) Long idServico){
		byte [] qrCode = servicoService.gerarQrCode(idServico);
		return ResponseEntity.ok(qrCode);
	}
	
}
