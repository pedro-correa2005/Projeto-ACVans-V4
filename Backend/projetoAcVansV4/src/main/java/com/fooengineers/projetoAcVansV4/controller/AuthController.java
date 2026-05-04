package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.LoginRequestDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.security.CustomUserDetailsService;
import com.fooengineers.projetoAcVansV4.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDto){
		//Verifica credenciais e pega usuário
		String email = loginDto.getEmail();
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						email,
						loginDto.getSenha()
						)
				);
		Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(email);
		
		//Se autenticação de dois fatores estiver desligada gera cookies e envia
		if (!usuario.isDoisFatores()) {
			ResponseCookie accessCookie = jwtService.gerarAccessCookie(email);
			ResponseCookie refreshCookie = jwtService.gerarRefreshCookie(email, System.currentTimeMillis());
			ResponseCookie csrfCookie =  jwtService.gerarCsrfCookie();
			return ResponseEntity.ok()
					.header(HttpHeaders.SET_COOKIE , accessCookie.toString())
					.header(HttpHeaders.SET_COOKIE , refreshCookie.toString())
					.header(HttpHeaders.SET_COOKIE , csrfCookie.toString())
					.body("Login Ok");
		}
		return null;
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request){
		//Pega o token no cookie da requisição
		String refreshToken = jwtService.getTokenFromCookies(request, "refresh_token");
		
		//Verifica se o token está no cookie
		if(refreshToken == null) {
			return ResponseEntity.status(401).body("Refresh token ausente");
		}
		
		//Verifica se a chave do token é válida e se o token já não expirou
		if(!jwtService.isValid(refreshToken, "refresh_token")) {
			return ResponseEntity.status(401).body("Refresh token inválido");
		}
		
		//Validação no redis, caso ambas validações tenham sido burladas
		if(!jwtService.validaRedis(refreshToken)) {
			return ResponseEntity.status(401).body("Refresh token revogado ou expirado");
		}
		
		String username = jwtService.extractUsername(refreshToken, "refresh_token");
		long createdAt = jwtService.getCreatedAt(refreshToken);

		//Rotação: invalida token antigo
		jwtService.deleteToken(refreshToken);
		
		//Cria novos cookies
		ResponseCookie accessCookie = jwtService.gerarAccessCookie(username);
		ResponseCookie refreshCookie = jwtService.gerarRefreshCookie(username, createdAt); //Tempo de criação do token anterior é repassado
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, accessCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
				.body("Token Renovado");
	}
}
