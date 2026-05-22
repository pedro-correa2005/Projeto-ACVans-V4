package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicoReqDTO {
	@NotNull(message = "Tipo de serviço é obrigatório")
	private Long idTipoServico;
	@NotNull(message = "Receber Notificação: Selecione uma opção")
	private boolean receberNotificacao;
	@NotNull(message = "Status de serviço é obrigatório")
	private Integer idStatusServico;
	@NotNull(message = "Veículo é obrigatório")
	private Long idVeiculo;
}
