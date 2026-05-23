package com.fooengineers.projetoAcVansV4.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.auditoria.formatter.AuditoriaFormatter;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.dto.ClienteReqDTO;
import com.fooengineers.projetoAcVansV4.dto.ClienteResDTO;
import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.exception.ClienteNaoEncontradoExcepiton;
import com.fooengineers.projetoAcVansV4.repository.ClienteRepository;
import com.fooengineers.projetoAcVansV4.specification.ClienteSpecification;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private AuditoriaService auditoriaService;
		
	public Page<ClienteResDTO> listar(Oficina oficina, Pageable pageable){
		return clienteRepository.findByOficina(oficina, pageable).map(ClienteResDTO::new);
	}
	
	public ClienteResDTO buscar(Long idCliente) {
		Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new ClienteNaoEncontradoExcepiton(idCliente));
		return new ClienteResDTO(cliente);
	}
	
	public Page<ClienteResDTO> buscar(Integer idOficina, String termo, Pageable pageable){
		Page<Cliente> clientes = clienteRepository.findAll(ClienteSpecification.filtroGeral(termo, idOficina), pageable);
		return clientes.map(ClienteResDTO::new);
	}
	
	public ClienteResDTO criar(ClienteReqDTO dto, Oficina oficina) {
		Cliente cliente = new Cliente();
		cliente.setNome(dto.getNome());
		cliente.setCelular(dto.getCelular());
		cliente.setOficina(oficina);
		Cliente salvo = clienteRepository.save(cliente);
		
		Map<String, Object> detalhes = AuditoriaFormatter.formatarCliente(cliente);
		auditoriaService.registrar(
				Acao.CREATE,
				Entidade.CLIENTE,
				cliente.getId(),
				detalhes);
		return new ClienteResDTO(salvo);
	}

	public ClienteResDTO atualizar(ClienteReqDTO dto, Long idCliente) {
		Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new ClienteNaoEncontradoExcepiton(idCliente));
		
		Map<String, Object> antes = AuditoriaFormatter.formatarCliente(cliente);

		cliente.setNome(dto.getNome());
		cliente.setCelular(dto.getCelular());
		
		Cliente salvo = clienteRepository.save(cliente);
		
		Map<String, Object> depois = AuditoriaFormatter.formatarCliente(salvo);
		
		Map<String, Object> alteracoes = new LinkedHashMap<>();
		
		alteracoes.put("antes", antes);
		alteracoes.put("depois", depois);
		
		auditoriaService.registrar(
				Acao.UPDATE,
				Entidade.CLIENTE,
				idCliente,
				alteracoes);
		
		return new ClienteResDTO(salvo);
	}
	public void deletar(Long idCliente) {
		Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new ClienteNaoEncontradoExcepiton(idCliente));
		clienteRepository.delete(cliente);
		Map<String, Object> detalhes = AuditoriaFormatter.formatarCliente(cliente);
		auditoriaService.registrar(
				Acao.DELETE,
				Entidade.CLIENTE,
				cliente.getId(),
				detalhes);
	}
}
