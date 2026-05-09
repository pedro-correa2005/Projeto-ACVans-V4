package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.entity.StatusServico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusServicoDTO {
	private int id;
	private String descricao;
	
	//Constructor a partir de entity.StatusServico
	public StatusServicoDTO(StatusServico status) {
		this.id = status.getId();
		this.descricao = status.getDescricao();
	}
}
