package com.fooengineers.projetoAcVansV4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário com email " + username + " não encontrado."));
	}

}
