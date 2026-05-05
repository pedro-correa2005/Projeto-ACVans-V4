package com.fooengineers.projetoAcVansV4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.security.CustomUserDetailsService;


@Service
public class AuthService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	public Usuario validarCredenciais(String email, String senha) {
		//Verifica credenciais e pega usuário
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));
		}catch (AuthenticationException e) {
			return null;
		}
		return (Usuario) userDetailsService.loadUserByUsername(email);
	}
}
