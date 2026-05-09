package com.fooengineers.projetoAcVansV4.dto;

import java.util.HashSet;
import java.util.Set;

import com.fooengineers.projetoAcVansV4.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResDTO {
	private Long id;
	private String email;
	private String doisFatores;
	private Set<String> roles;
	private int idOficina;
	
	//Constructor a partir de Entity.Usuario
	public UsuarioResDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.email = usuario.getEmail();
		this.doisFatores = usuario.isDoisFatores()?"Ativada":"Desativada";
		this.roles = new HashSet<String>();
		
		usuario.getRoles().forEach((role) -> {
			roles.add(role.getNome());
		});
		
		this.idOficina = usuario.getOficina().getId();
	}
}
