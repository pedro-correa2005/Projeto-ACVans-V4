package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.UsuarioResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;

@RestController
@RequestMapping("/api")
public class UsuarioController {
	@GetMapping("/detalhes-usuario")
	public ResponseEntity<UsuarioResDTO> detalhesUsuario(Authentication authentication) {
		Usuario usuario = (Usuario) authentication.getPrincipal();
		UsuarioResDTO dto = new UsuarioResDTO(usuario);
		return ResponseEntity.ok(dto);
	}
}
