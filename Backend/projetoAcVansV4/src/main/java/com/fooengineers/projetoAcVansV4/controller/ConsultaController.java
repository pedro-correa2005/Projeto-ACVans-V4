package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.ConsultaResDTO;
import com.fooengineers.projetoAcVansV4.service.ConsultaService;

@RestController
@RequestMapping("/api/consulta")
public class ConsultaController {
	@Autowired
	private ConsultaService consultaService;
	
	@GetMapping
	public ResponseEntity<ConsultaResDTO> consultarPorTokenPlaca(@RequestParam String token, @RequestParam String placa){
		ConsultaResDTO dto = consultaService.consultar(token, placa);
		return dto == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(dto);
	}
	@GetMapping("/{token}")
	public ResponseEntity<ConsultaResDTO> consultarPorToken(@PathVariable String token){
		ConsultaResDTO dto = consultaService.consultar(token);
		return dto == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(dto);
	}
}
