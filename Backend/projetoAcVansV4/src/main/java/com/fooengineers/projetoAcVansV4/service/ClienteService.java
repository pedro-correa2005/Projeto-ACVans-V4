package com.fooengineers.projetoAcVansV4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.ClienteReqDTO;
import com.fooengineers.projetoAcVansV4.dto.ClienteResDTO;
import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.repository.ClienteRepository;

@Service
public class ClienteService {
	@Autowired
	ClienteRepository clienteRepository;
	
	public Page<ClienteResDTO> listar(Oficina oficina, Pageable pageable){
		return clienteRepository.findByOficina(oficina, pageable).map(ClienteResDTO::new);
	}
	
	public ClienteResDTO criar(ClienteReqDTO dto, Oficina oficina) {
		Cliente cliente = new Cliente();
		cliente.setNome(dto.getNome());
		cliente.setCelular(dto.getCelular());
		cliente.setOficina(oficina);
		Cliente salvo = clienteRepository.save(cliente);
		return new ClienteResDTO(salvo);
	}
}
