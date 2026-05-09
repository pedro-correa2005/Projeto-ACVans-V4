package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdemDTO {
	@NotNull(message="Id de etapa é obrigatório")
	Long idEtapaServico;
	@NotNull(message="Ordem anterior é obrigatória")
	int ordemAnterior;
	@NotNull(message="Próxima ordem é obrigatória")
	int ordemProxima;
}
