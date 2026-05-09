package com.fooengineers.projetoAcVansV4.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroReqDTO {
	@NotBlank(message = "Nome do cliente é obrigatório")
	private String nomeCliente;
	
	@NotBlank(message = "O número de celular é obrigatório")
	@Size(min = 14, max = 14, message = "O número de celular deve ter 14 caracteres")
	@Pattern(regexp = "^\\([1-9][1-9]\\)\\ 9[0-9]{8}$", message = "Número inválido.")
	private String celularCliente;
	
	@NotBlank(message = "A placa é obriatória")
	@Size(min = 7, max = 7, message = "A placa deve ter 7 caracteres")
	@Pattern(regexp = "^[A-Z]{3}[0-9]{1}[A-Z0-9]{1}[0-9]{2}$", message = "Placa Inválida")
	private String placaVeiculo;
	
	@NotBlank(message = "Marca do veículo é obrigatório")
	private String marcaVeiculo;
	
	@NotBlank(message = "Modelo do veículo é obrigatório")
	private String modeloVeiculo;
	
	@NotNull(message = "Receber Notificação: Selecione uma opção")
	private boolean receberNotificacao;
	
	@NotNull(message = "Data de fim prevista é obrigatório")
	private Timestamp dataFim;
}
