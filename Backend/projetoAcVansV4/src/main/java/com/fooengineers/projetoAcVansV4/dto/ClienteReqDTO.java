package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteReqDTO {
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	@NotBlank(message = "Celular é obrigatório")
	@Size(min = 14, max = 14, message = "O número de celular deve ter 14 caracteres")
	@Pattern(regexp = "^\\([1-9][1-9]\\)\\ 9[0-9]{8}$", message = "Número inválido.")
	private String celular;
}
