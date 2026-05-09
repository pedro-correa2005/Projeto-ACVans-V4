package com.fooengineers.projetoAcVansV4.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaDTO {
	private String tipoServicoDescricao;
	private List<EtapaServicoResponseDTO> etapas;
	private Long idEtapaServico;
}
