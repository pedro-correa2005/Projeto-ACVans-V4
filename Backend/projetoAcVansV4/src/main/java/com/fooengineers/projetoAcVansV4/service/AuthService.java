package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.security.CustomUserDetailsService;

import jakarta.mail.MessagingException;


@Service
public class AuthService {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private SmtpEmailService emailService;
	@Autowired
	private PasswordPolicyService passwordPolicyService;
	
	public Usuario validarCredenciais(String email, String senha) {
		//Verifica credenciais e pega usuário
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));
		}catch (AuthenticationException e) {
			return null;
		}
		return (Usuario) userDetailsService.loadUserByUsername(email);
	}
	
	public boolean validarTentativas(String ip, String email) {
		Long ipAttempts = redisService.increment("login:ip:" + ip, 60);
		Long userAttempts = redisService.increment("login:user:" + email, 300);
		
		if(ipAttempts > 20 || userAttempts > 5) {
			return false;
		}
		return true;
	}
	
	public List<String> validarNovaSenha(String senha, Usuario usuario) {		
		return passwordPolicyService.validate(senha, usuario.getRoles());
	}
	
	public void esqueciSenha(String email) {
		//Valida Email
		try{
			userDetailsService.loadUserByUsername(email);
		} catch (UsernameNotFoundException e) {
			System.err.println(e);
			return;
		}
		//Gera token e salva no banco relacionado a email
		String token = UUID.randomUUID().toString();
		
		redisService.save("reset:" + token, email, 900);
		//Envia o email de recuperação
		emailService.enviarResetSenha(email, token);
	}
	
	public Usuario validarResetToken(String token) {
		String email = redisService.get("reset:"+token);
		return (Usuario) userDetailsService.loadUserByUsername(email);
	}
}
