package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedefinirSenhaDTO {
	@NotBlank(message = "Token é obrigatório")
	private String token;
	@NotBlank(message = "Nova senha é obrigatória")
	private String novaSenha;
	@NotBlank(message = "Repetir nova senha é obrigatória")
	private String repetirNovaSenha;
}
