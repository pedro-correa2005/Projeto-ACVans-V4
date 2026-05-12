package com.fooengineers.projetoAcVansV4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.CadastroReqDTO;
import com.fooengineers.projetoAcVansV4.dto.CadastroResDTO;
import com.fooengineers.projetoAcVansV4.repository.ServicoRepository;
import com.fooengineers.projetoAcVansV4.specification.ServicoSpecification;

@Service
public class CadastroService {
	@Autowired
	ServicoRepository servicoRepository;
	
	public Page<CadastroResDTO> listar(Integer idStatusServico, String param, Integer idOficina, Pageable pageable){
		return servicoRepository.findAll(ServicoSpecification.filtroGeral(param, idOficina, idStatusServico), pageable).map(CadastroResDTO::new);
	}
	public CadastroResDTO atualizar(CadastroReqDTO dto) {
		return null;
	}
	public void deletar(Long idServico) {
		
	}
}
