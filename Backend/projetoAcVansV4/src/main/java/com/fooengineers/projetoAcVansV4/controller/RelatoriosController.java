package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.MediaEtapasDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.HistoricoEtapaService;

@RestController
@RequestMapping("/api/relatorios")
public class RelatoriosController {
	@Autowired
	private HistoricoEtapaService historicoEtapaService;
	@GetMapping("/media-etapas")
	public List<MediaEtapasDTO> mediaEtapas(Authentication authentication, @RequestParam int ano, @RequestParam int mes){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return historicoEtapaService.calcularMediaEtapas(ano, mes, usuario.getOficina());
	}
}
