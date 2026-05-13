package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.VeiculoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.VeiculoService;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {
	@Autowired
	private VeiculoService veiculoService;
	
	@GetMapping
	public Page<VeiculoResDTO> listar(Authentication authentication, @RequestParam(defaultValue="") String termo, Pageable pageable){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return veiculoService.listar(usuario.getOficina().getId(), termo, pageable);
	}
}
