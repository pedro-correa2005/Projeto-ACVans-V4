package com.fooengineers.projetoAcVansV4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdemDTO {
	private Integer ordemAnterior;
	private Integer ordemProxima;
}
