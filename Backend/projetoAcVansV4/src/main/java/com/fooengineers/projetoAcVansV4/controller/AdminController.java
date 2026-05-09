package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.OficinaResDTO;
import com.fooengineers.projetoAcVansV4.service.OficinaService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	OficinaService oficinaService;
	@GetMapping("/oficinas")
	public List<OficinaResDTO> listar(@RequestParam(defaultValue="") String param){
		return oficinaService.listar(param);
	}
}
