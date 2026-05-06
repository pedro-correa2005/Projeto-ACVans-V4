package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
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
	private smtpEmailService emailService;
	
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
	
	//TODO complexar validação
	public boolean validarNovaSenha(String senha) {
		if(senha.length() < 8) {
			return false;
		}
		return true;
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
		try {
			emailService.enviarResetSenha(email, token);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Usuario validarResetToken(String token) {
		String email = redisService.get("reset:"+token);
		return (Usuario) userDetailsService.loadUserByUsername(email);
	}
}
