package com.fooengineers.projetoAcVansV4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Usuario alterarSenha(Usuario u, String novaSenha) {
		u.setSenha(passwordEncoder.encode(novaSenha));
		return usuarioRepository.save(u);
	}
}
