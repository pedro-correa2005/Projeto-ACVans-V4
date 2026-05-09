package com.fooengineers.projetoAcVansV4.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioReqDTO {
	private String email;
	private Set<String> roles;
}
