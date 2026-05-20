package com.fooengineers.projetoAcVansV4.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.RoleResDTO;
import com.fooengineers.projetoAcVansV4.dto.RolesReqDTO;
import com.fooengineers.projetoAcVansV4.dto.UsuarioReqDTO;
import com.fooengineers.projetoAcVansV4.dto.UsuarioResDTO;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Role;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.exception.EmailEmUsoException;
import com.fooengineers.projetoAcVansV4.exception.OficinaNaoEncontradaException;
import com.fooengineers.projetoAcVansV4.exception.RoleInvalidoException;
import com.fooengineers.projetoAcVansV4.exception.UsuarioNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.OficinaRepository;
import com.fooengineers.projetoAcVansV4.repository.RoleRepository;
import com.fooengineers.projetoAcVansV4.repository.UsuarioRepository;
import com.fooengineers.projetoAcVansV4.specification.UsuarioSpecification;
import com.fooengineers.projetoAcVansV4.util.SenhaUtil;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private OficinaRepository oficinaRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private SmtpEmailService emailService;
	
	public Page<UsuarioResDTO> listarPorOficina(Long idOficina, String param, Pageable pageable){
		return usuarioRepository.findAll(UsuarioSpecification.filtroGeral(idOficina, param), pageable).map(UsuarioResDTO::new);
	}
	
	public UsuarioResDTO criar(Integer idOficina, UsuarioReqDTO dto) {
		Oficina oficina = oficinaRepository.findById(idOficina).orElseThrow(() -> new OficinaNaoEncontradaException(idOficina));

		Set<Role> roles = dto.getRoles().stream()
				.map(nome -> roleRepository.findByNome(nome)
						.orElseThrow(() -> new RoleInvalidoException(nome)))
				.collect(Collectors.toSet());
		
		String email = dto.getEmail();
		
		if(usuarioRepository.existsByEmail(email)) throw new EmailEmUsoException(email);
		
		String senhaInicial = SenhaUtil.gerarSenha(12);
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setPrimeiroLogin(true);
		usuario.setSenha(passwordEncoder.encode(senhaInicial));
		usuario.setDoisFatores(dto.getDoisFatores());
		usuario.setRoles(roles);
		usuario.setOficina(oficina);
		Usuario criado = usuarioRepository.save(usuario);
		emailService.enviarSenhaInicial(email, senhaInicial);
		return new UsuarioResDTO(criado);
	}
	
	public void criarAdmin(String email) {
		Usuario u = new Usuario();
        u.setEmail(email);
        if(usuarioRepository.existsByEmail(u.getEmail())) {
        	return;
        }
        String senhaInicial = SenhaUtil.gerarSenha(12);
        u.setSenha(passwordEncoder.encode(senhaInicial));
        u.setPrimeiroLogin(true);
        u.setDoisFatores(true);
        Set<Role> roles = new HashSet<Role>();
        roles.add(new Role(1, "ADMIN"));
        u.setRoles(roles);

        usuarioRepository.save(u);
        emailService.enviarSenhaInicial(email, senhaInicial);
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

	public UsuarioResDTO atualizarRoles(Long idUsuario, RolesReqDTO dto) {
		Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
		Set<Role> roles = dto.getRoles().stream()
				.map(nome -> roleRepository.findByNome(nome)
						.orElseThrow(() -> new RoleInvalidoException(nome)))
				.collect(Collectors.toSet());
		usuario.setRoles(roles);
		Usuario atualizado = usuarioRepository.save(usuario);
		return new UsuarioResDTO(atualizado);
	}

	public void deletar(Long idUsuario) {
		Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new UsuarioNaoEncontradoException(idUsuario));
		usuarioRepository.delete(usuario);
	}

	public List<RoleResDTO> listarRoles() {
		List<RoleResDTO> dto = roleRepository.findAll().stream().map(RoleResDTO::new).collect(Collectors.toList());
		return dto;
	}
}
