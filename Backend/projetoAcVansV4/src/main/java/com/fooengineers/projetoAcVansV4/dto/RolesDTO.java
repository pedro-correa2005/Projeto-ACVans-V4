package com.fooengineers.projetoAcVansV4.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesDTO {
	@NotEmpty(message="Escolha ao menos um perfil de usuário")
	private Set<String> roles;
}
