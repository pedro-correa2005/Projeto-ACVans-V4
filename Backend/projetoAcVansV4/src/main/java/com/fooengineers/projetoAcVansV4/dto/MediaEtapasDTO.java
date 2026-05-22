package com.fooengineers.projetoAcVansV4.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MediaEtapasDTO {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Etapa{		
		protected String etapaTitulo;
		protected float mediaMinutos;
	}
	
	private List<Etapa> etapas;
	private String tipoServicoDescricao;
}
