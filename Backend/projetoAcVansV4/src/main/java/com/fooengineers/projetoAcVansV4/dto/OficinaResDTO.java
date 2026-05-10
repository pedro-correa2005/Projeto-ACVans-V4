package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.entity.Oficina;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OficinaResDTO {
	private int id;
	private String nome;
	private String ativo;
	
	public OficinaResDTO(Oficina oficina) {
		this.id = oficina.getId();
		this.nome = oficina.getNome();
		this.ativo = oficina.getAtivo()?"Sim":"Não";
	}
}
