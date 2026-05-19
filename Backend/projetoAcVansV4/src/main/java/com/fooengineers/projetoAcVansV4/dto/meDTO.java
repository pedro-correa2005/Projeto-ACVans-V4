package com.fooengineers.projetoAcVansV4.dto;

import java.util.HashSet;
import java.util.Set;

import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class meDTO {
	private Long id;
	private String email;
	private boolean doisFatores;
	private boolean primeiroLogin;
	private PolicyDTO passwordPolicy;
	private Set<String> roles;
	private Integer idOficina;
	
	//Constructor a partir de Entity.Usuario
	public meDTO(Usuario usuario, PolicyDTO policy) {
		this.id = usuario.getId();
		this.email = usuario.getEmail();
		this.doisFatores = usuario.isDoisFatores();
		this.primeiroLogin = usuario.isPrimeiroLogin();
		this.passwordPolicy =  policy;
		this.roles = new HashSet<String>();
		
		usuario.getRoles().forEach((role) -> {
			roles.add(role.getNome());
		});
		Oficina oficina = usuario.getOficina();
		this.idOficina = oficina != null? oficina.getId() : null;
	}
}
