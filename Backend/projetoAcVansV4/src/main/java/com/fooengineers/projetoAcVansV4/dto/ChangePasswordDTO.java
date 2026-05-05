package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
	@NotBlank(message = "Senha atual é obrigatória")
	private String senhaAtual;
	@NotBlank(message = "Nova senha é obrigatória")
	private String novaSenha;
	@NotBlank(message = "Repetir nova senha é obrigatória")
	private String repetirNovaSenha;
}
