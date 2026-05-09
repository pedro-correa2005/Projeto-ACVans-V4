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
public class CadastroReqDTO {
	@NotNull(message = "Nome do cliente é obrigatório")
	private String nomeCliente;
	@NotNull(message = "Celular do cliente é obrigatório")
	private String celularCliente;
	@NotNull(message = "Placa do veículo é obrigatório")
	private String placaVeiculo;
	@NotNull(message = "Marca do veículo é obrigatório")
	private String marcaVeiculo;
	@NotNull(message = "Modelo do veículo é obrigatório")
	private String modeloVeiculo;
	@NotBlank(message = "Receber Notificação: Selecione uma opção")
	private boolean receberNotificacao;
	@NotNull(message = "Data de fim prevista é obrigatório")
	private Timestamp dataFim;
}
