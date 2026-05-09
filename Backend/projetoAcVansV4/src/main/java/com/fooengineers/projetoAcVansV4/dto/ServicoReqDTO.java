package com.fooengineers.projetoAcVansV4.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicoReqDTO {
	@NotBlank(message = "Tipo de serviço é obrigatório")
	private Long idTipoServico;
	@NotNull(message = "Receber Notificação: Selecione uma opção")
	private boolean receberNotificacao;
	@NotBlank(message = "Tipo de serviço é obrigatório")
	private Long idStatusServico;
	@NotNull(message = "Data de fim prevista é obrigatória")
	private Timestamp dataFim;
}
