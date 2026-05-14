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

import com.fooengineers.projetoAcVansV4.auditoria.annotation.Auditavel;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.dto.ClienteReqDTO;
import com.fooengineers.projetoAcVansV4.dto.ClienteResDTO;
import com.fooengineers.projetoAcVansV4.dto.VeiculoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.ClienteService;
import com.fooengineers.projetoAcVansV4.service.VeiculoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private VeiculoService veiculoService;
	

	@GetMapping
	public Page<ClienteResDTO> buscar(Authentication authentication, @RequestParam(defaultValue="") String termo, Pageable pageable){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		return clienteService.buscar(usuario.getOficina().getId(), termo, pageable);
	}
	
	@GetMapping("/{idCliente}")
	public ResponseEntity<ClienteResDTO> buscar(@PathVariable(required=true) Long idCliente){
		ClienteResDTO cliente = clienteService.buscar(idCliente);
		return ResponseEntity.ok().body(cliente);
	}
	
	@PostMapping
	@Auditavel(acao = Acao.CREATE, entidade = Entidade.CLIENTE)
	public ResponseEntity<ClienteResDTO> criar(@ModelAttribute @Valid ClienteReqDTO dto, Authentication authentication){
		Usuario usuario = (Usuario) authentication.getPrincipal();
		ClienteResDTO criado = clienteService.criar(dto, usuario.getOficina());
		return ResponseEntity.ok().body(criado);
	}
	
	@PutMapping("/{idCliente}")
	public ResponseEntity<ClienteResDTO> atualizar(@ModelAttribute @Valid ClienteReqDTO dto,@PathVariable(required=true) Long idCliente){
		ClienteResDTO atualizado = clienteService.atualizar(dto, idCliente);
		return ResponseEntity.ok().body(atualizado);
	}

	@DeleteMapping("/{idCliente}")
	@Auditavel(acao = Acao.DELETE, entidade = Entidade.CLIENTE)
	public ResponseEntity<ClienteResDTO> deletar(@PathVariable(required=true) Long idCliente){
		clienteService.deletar(idCliente);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{idCliente}/veiculos")
	public List<VeiculoResDTO> buscarVeiculos(@PathVariable(required=true) Long idCliente, @RequestParam(defaultValue="") String termo){
		return veiculoService.buscarPorCliente(idCliente, termo);
	}
}
