package com.fooengineers.projetoAcVansV4.entity;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "usuario_role",
			joinColumns = @JoinColumn(name = "fk_usuario"),
			inverseJoinColumns = @JoinColumn(name = "fk_role")
	)
	private Set<Role> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNome()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}
}
