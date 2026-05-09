package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.entity.EtapaServico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtapaServicoResDTO {
	private Long id;
	private Integer ordem;
	private String titulo;
	private String descricao;
	private Long idTipoServico;
	private Integer idOficina;
	
	//Construtor a partir de Entity.EtapaServico
	public EtapaServicoResDTO(EtapaServico etapa) {
		this.id = etapa.getId();
		this.ordem = etapa.getOrdem();
		this.titulo = etapa.getTitulo();
		this.descricao = etapa.getDescricao();
		this.idTipoServico = etapa.getTipoServico().getId();
		this.idOficina = etapa.getOficina().getId();
	}
}
