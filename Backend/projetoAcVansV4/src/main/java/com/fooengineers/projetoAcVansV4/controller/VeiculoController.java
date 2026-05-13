package com.fooengineers.projetoAcVansV4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.ServicoResDTO;
import com.fooengineers.projetoAcVansV4.dto.VeiculoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.VeiculoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.ServicoService;
import com.fooengineers.projetoAcVansV4.service.VeiculoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {
	@Autowired
	private VeiculoService veiculoService;
	@Autowired
	private ServicoService servicoService;
	
	@GetMapping
	public Page<VeiculoResDTO> listar(Authentication authentication, @RequestParam(defaultValue="") String termo, Pageable pageable){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return veiculoService.listar(usuario.getOficina().getId(), termo, pageable);
	}
	
	@GetMapping("/{idVeiculo}")
	public ResponseEntity<VeiculoResDTO> buscar(@PathVariable(required=true) Long idVeiculo){
		VeiculoResDTO veiculo = veiculoService.buscar(idVeiculo);
		return ResponseEntity.ok(veiculo);
	}
	
	@PostMapping
	public ResponseEntity<VeiculoResDTO> criar(Authentication authentication, @ModelAttribute @Valid VeiculoReqDTO dto) {
		Usuario usuario = (Usuario) authentication.getPrincipal();
		VeiculoResDTO criado = veiculoService.criar(dto, usuario.getOficina());
		return ResponseEntity.ok(criado);
	}
	
	@PutMapping("/{idVeiculo}")
	public ResponseEntity<VeiculoResDTO> atualizar(@ModelAttribute @Valid VeiculoReqDTO dto, @PathVariable(required=true) Long idVeiculo) {
		VeiculoResDTO atualizado = veiculoService.atualizar(dto, idVeiculo);
		return ResponseEntity.ok(atualizado);
	}
	
	@DeleteMapping("/{idVeiculo}")
	public ResponseEntity<?> delete(@PathVariable(required=true) Long idVeiculo){
		veiculoService.deletar(idVeiculo);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{idVeiculo}/servicos")
	public List<ServicoResDTO> buscar(@PathVariable(required=true) Long idVeiculo, @RequestParam(defaultValue="") String termo){
		return servicoService.buscarPorVeiculo(idVeiculo, termo);
	}
}
