package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OficinaReqDTO {
	@NotBlank(message="Nome da oficina é obrigatório")
	private String nome;
	@NotNull
	private boolean ativo = false;
}
