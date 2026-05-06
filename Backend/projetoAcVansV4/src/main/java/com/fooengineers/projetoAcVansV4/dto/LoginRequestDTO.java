package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
	@NotBlank(message = "Email é obrigatório")
	@Pattern(
	        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", 
	        message = "E-mail inválido"
	    )
	private String email;
	@NotBlank(message = "Senha é obrigatória")
	private String senha;
}
