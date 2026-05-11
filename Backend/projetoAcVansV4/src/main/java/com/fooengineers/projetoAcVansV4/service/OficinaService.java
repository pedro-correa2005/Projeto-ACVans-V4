package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.OficinaReqDTO;
import com.fooengineers.projetoAcVansV4.dto.OficinaResDTO;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.exception.OficinaNaoEncontradaException;
import com.fooengineers.projetoAcVansV4.repository.OficinaRepository;
import com.fooengineers.projetoAcVansV4.specification.OficinaSpecification;

@Service
public class OficinaService {
	@Autowired
	OficinaRepository oficinaRepository;
	
	public List<OficinaResDTO> listar(String param){
		return oficinaRepository.findAll(OficinaSpecification.filtroGeral(param)).stream()
				.map(OficinaResDTO::new)
				.collect(Collectors.toList());
		
	}
	
	public OficinaResDTO criar(OficinaReqDTO dto) {
		Oficina oficina = new Oficina();
		oficina.setNome(dto.getNome());
		oficina.setAtivo(dto.getAtivo());
		Oficina criado = oficinaRepository.save(oficina);
		return new OficinaResDTO(criado);
	}
	
	public OficinaResDTO atualizar(OficinaReqDTO dto, Integer idOficina) {
		Oficina oficina = oficinaRepository.findById(idOficina).orElseThrow(() -> new OficinaNaoEncontradaException(idOficina));
		oficina.setNome(dto.getNome());
		oficina.setAtivo(dto.getAtivo());
		Oficina atualizado = oficinaRepository.save(oficina);
		return new OficinaResDTO(atualizado);
	}
	
	public void deletar(Integer idOficina) {
		Oficina oficina = oficinaRepository.findById(idOficina).orElseThrow(() -> new OficinaNaoEncontradaException(idOficina));
		oficinaRepository.delete(oficina);
	}
}
