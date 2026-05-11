package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.OficinaReqDTO;
import com.fooengineers.projetoAcVansV4.dto.OficinaResDTO;
import com.fooengineers.projetoAcVansV4.dto.RolesDTO;
import com.fooengineers.projetoAcVansV4.dto.UsuarioReqDTO;
import com.fooengineers.projetoAcVansV4.dto.UsuarioResDTO;
import com.fooengineers.projetoAcVansV4.service.OficinaService;
import com.fooengineers.projetoAcVansV4.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	OficinaService oficinaService;
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping("/oficinas")
	public List<OficinaResDTO> listar(@RequestParam(defaultValue="") String param){
		return oficinaService.listar(param);
	}
	
	@PostMapping("/oficinas")
	public ResponseEntity<OficinaResDTO> criar(@ModelAttribute @Valid OficinaReqDTO dto){
		OficinaResDTO criado = oficinaService.criar(dto);
		return ResponseEntity.ok(criado);
	}
	
	@PutMapping("/oficinas/{idOficina}")
	public ResponseEntity<OficinaResDTO> atualizar(@ModelAttribute @Valid OficinaReqDTO dto, @PathVariable(required=true) Integer idOficina){
		OficinaResDTO atualizado = oficinaService.atualizar(dto, idOficina);
		return ResponseEntity.ok(atualizado);
	}
	
	@DeleteMapping("/oficinas/{idOficina}")
	public ResponseEntity<?> deletar(@PathVariable(required=true) Integer idOficina){
		oficinaService.deletar(idOficina);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/oficinas/{idOficina}/usuarios")
	public List<UsuarioResDTO> listar(@PathVariable Long idOficina, @RequestParam(defaultValue="") String param){
		return usuarioService.listarPorOficina(idOficina, param);
	}
	
	@PostMapping("/oficinas/{idOficina}/usuarios")
	public ResponseEntity<UsuarioResDTO> criar(@PathVariable Integer idOficina, @ModelAttribute @Valid UsuarioReqDTO dto){
		UsuarioResDTO criado = usuarioService.criar(idOficina, dto);
		return ResponseEntity.ok(criado);
	}
	
	@PutMapping("/usuarios/{idUsuario}/atualizar-roles")
	public ResponseEntity<UsuarioResDTO> atualizarRoles(@PathVariable Long idUsuario, @ModelAttribute @Valid RolesDTO dto){
		UsuarioResDTO atualizado = usuarioService.atualizarRoles(idUsuario, dto);
		return ResponseEntity.ok(atualizado);
	}
}
