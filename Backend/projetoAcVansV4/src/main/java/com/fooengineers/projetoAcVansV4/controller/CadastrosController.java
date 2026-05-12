package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.CadastroResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.CadastroService;

@RestController("/api/cadastros")
public class CadastrosController {
	@Autowired
	CadastroService cadastroService;
	
	@GetMapping
	public Page<CadastroResDTO> listar(@RequestParam Integer idStatusServico, @RequestParam(defaultValue="") String param, Authentication authentication, Pageable pageable){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return cadastroService.listar(idStatusServico, param, usuario.getOficina().getId(), pageable);
	}
}
