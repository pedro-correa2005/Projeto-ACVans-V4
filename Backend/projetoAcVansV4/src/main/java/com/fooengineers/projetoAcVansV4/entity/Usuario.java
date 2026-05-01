package com.fooengineers.projetoAcVansV4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	@Column(name = "primeiro_login")
	private boolean primeiroLogin = true;
	
	@Column(name = "dois_fatores")
	private boolean doisFatores = true;
	
	@ManyToOne
	@JoinColumn(name = "fk_oficina", nullable = true)
	private Oficina oficina;
}
