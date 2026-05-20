package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.OficinaReqDTO;
import com.fooengineers.projetoAcVansV4.dto.OficinaResDTO;
import com.fooengineers.projetoAcVansV4.dto.RoleResDTO;
import com.fooengineers.projetoAcVansV4.dto.RolesReqDTO;
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
	public Page<OficinaResDTO> listar(@RequestParam(defaultValue="") String termo, Pageable pageable){
		return oficinaService.listar(termo, pageable);
	}
	
	@PostMapping("/oficinas")
	public ResponseEntity<OficinaResDTO> criar(@RequestBody @Valid OficinaReqDTO dto){
		OficinaResDTO criado = oficinaService.criar(dto);
		return ResponseEntity.ok(criado);
	}
	
	@PutMapping("/oficinas/{idOficina}")
	public ResponseEntity<OficinaResDTO> atualizar(@RequestBody @Valid OficinaReqDTO dto, @PathVariable(required=true) Integer idOficina){
		OficinaResDTO atualizado = oficinaService.atualizar(dto, idOficina);
		return ResponseEntity.ok(atualizado);
	}
	
	@DeleteMapping("/oficinas/{idOficina}")
	public ResponseEntity<?> deletar(@PathVariable(required=true) Integer idOficina){
		oficinaService.deletar(idOficina);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/oficinas/{idOficina}/usuarios")
	public Page<UsuarioResDTO> listar(@PathVariable Long idOficina, @RequestParam(defaultValue="") String param, Pageable pageable){
		return usuarioService.listarPorOficina(idOficina, param, pageable);
	}
	
	@PostMapping("/oficinas/{idOficina}/usuarios")
	public ResponseEntity<UsuarioResDTO> criar(@PathVariable Integer idOficina, @RequestBody @Valid UsuarioReqDTO dto){
		UsuarioResDTO criado = usuarioService.criar(idOficina, dto);
		return ResponseEntity.ok(criado);
	}
	
	@PutMapping("/usuarios/{idUsuario}/atualizar-roles")
	public ResponseEntity<UsuarioResDTO> atualizarRoles(@PathVariable Long idUsuario, @RequestBody @Valid RolesReqDTO dto){
		UsuarioResDTO atualizado = usuarioService.atualizarRoles(idUsuario, dto);
		return ResponseEntity.ok(atualizado);
	}
	
	@DeleteMapping("/usuarios/{idUsuario}")
	public ResponseEntity<?> deletarUsuario(@PathVariable(required=true) Long idUsuario){
		usuarioService.deletar(idUsuario);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/roles")
	public List<RoleResDTO> listarRoles(){
		return usuarioService.listarRoles();
	}
}
