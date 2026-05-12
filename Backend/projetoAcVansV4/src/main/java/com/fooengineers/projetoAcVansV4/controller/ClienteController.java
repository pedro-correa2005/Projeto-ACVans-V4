package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.ClienteReqDTO;
import com.fooengineers.projetoAcVansV4.dto.ClienteResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping
	public Page<ClienteResDTO> listar(Authentication authentication, Pageable pageable){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return clienteService.listar(usuario.getOficina(), pageable);
	}
	
	@PostMapping
	public ResponseEntity<ClienteResDTO> criar(@ModelAttribute @Valid ClienteReqDTO dto, Authentication authentication){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		ClienteResDTO criado = clienteService.criar(dto, usuario.getOficina());
		return ResponseEntity.ok().body(criado);
	}
}
