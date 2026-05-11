package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
	public TipoServicoResDTO criar(@ModelAttribute @Valid TipoServicoReqDTO dto, Authentication authentication) {
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return tipoServicoService.criar(dto, usuario.getOficina());
	}
}
