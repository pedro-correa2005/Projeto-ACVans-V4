package com.fooengineers.projetoAcVansV4.dto;

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
public class VeiculoReqDTO {
	@NotBlank(message = "A placa é obriatória")
	@Size(min = 7, max = 7, message = "A placa deve ter 7 caracteres")
	@Pattern(regexp = "^[A-Z]{3}[0-9]{1}[A-Z0-9]{1}[0-9]{2}$", message = "Placa Inválida")
	private String placa;
	@NotBlank(message = "A marca é obriatória")
	private String marca;
	@NotBlank(message = "O modelo é obriatório")
	private String modelo;
	@NotNull(message = "Cliente é obrigatório")
	private Long idCliente;
}
