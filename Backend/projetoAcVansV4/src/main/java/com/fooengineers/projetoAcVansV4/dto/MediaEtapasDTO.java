package com.fooengineers.projetoAcVansV4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaEtapasDTO {
	private String tipoServicoDescricao;
	private String etapaTitulo;
	private float mediaMinutos;
}
