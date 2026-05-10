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
import com.fooengineers.projetoAcVansV4.service.OficinaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	OficinaService oficinaService;
	
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
	public ResponseEntity<OficinaResDTO> atualizar(@ModelAttribute @Valid OficinaReqDTO dto, @PathVariable(required=true) Long idOficina){
		OficinaResDTO atualizado = oficinaService.atualizar(dto, idOficina);
		return ResponseEntity.ok(atualizado);
	}
	
	@DeleteMapping("/oficinas/{idOficina}")
	public ResponseEntity<?> deletar(@PathVariable(required=true) Long idOficina){
		oficinaService.deletar(idOficina);
		return ResponseEntity.ok().build();
	}
}
