package com.fooengineers.projetoAcVansV4.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioReqDTO {
	@NotBlank(message = "Email é obrigatório")
	@Pattern(
	        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", 
	        message = "E-mail inválido"
	    )
	private String email;
	@NotNull(message = "Autenticação de dois fatores: Escolha uma opção")
	private Boolean doisFatores;
	@NotEmpty(message = "Ao menos uma role é obrigatória")
	private Set<String> roles;
}
