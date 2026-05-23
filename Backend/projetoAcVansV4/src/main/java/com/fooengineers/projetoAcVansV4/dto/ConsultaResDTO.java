package com.fooengineers.projetoAcVansV4.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaResDTO {
	private String tipoServicoDescricao;
	private List<EtapaServicoResDTO> etapas;
	private boolean finalizado;
	private Long idEtapaServico;
}
