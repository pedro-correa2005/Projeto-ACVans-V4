package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.UsuarioResDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.repository.UsuarioRepository;
import com.fooengineers.projetoAcVansV4.specification.UsuarioSpecification;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<UsuarioResDTO> listarPorOficina(Long idOficina, String param){
		return usuarioRepository.findAll(UsuarioSpecification.filtroGeral(idOficina, param)).stream()
				.map(UsuarioResDTO::new)
				.collect(Collectors.toList());
	}
	
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email).orElseThrow();
	}
	
	public Usuario alterarSenha(Usuario u, String novaSenha) {
		u.setSenha(passwordEncoder.encode(novaSenha));
		if(u.isPrimeiroLogin()) {
			u.setPrimeiroLogin(false);
		}
		return usuarioRepository.save(u);
	}
	
	public void ativarAutenticacao(Usuario usuario) {
		usuario.setDoisFatores(true);
		usuarioRepository.save(usuario);
	}
	public void desativarAutenticacao(Usuario usuario) {
		usuario.setDoisFatores(false);
		usuarioRepository.save(usuario);
	}
}
