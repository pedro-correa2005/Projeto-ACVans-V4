package com.fooengineers.projetoAcVansV4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.fooengineers.projetoAcVansV4.security.JwtService;
import com.fooengineers.projetoAcVansV4.service.RedisService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private RedisService redisService;
	
	@Value("${app.security.cookie.secure}")
	private boolean secure;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDto){
		String email = loginDto.getEmail();
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						email,
						loginDto.getSenha()
						)
				);
		
		String accessToken = jwtService.generateAccessToken(email);
		String refreshToken = jwtService.generateRefreshToken(email);
		
		//Salva no redis o identificador único jti:email com tempo de expiração igual ao token
		String jti = jwtService.extractJti(refreshToken);
		long ttl = 60 * 60 * 24;
		redisService.saveRefreshToken(jti, email, System.currentTimeMillis(), ttl);
		
		ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
				.httpOnly(true)
				.secure(secure)
				.sameSite("None")
				.path("/")
				.maxAge(60 * 10)
				.build();
		
		ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
				.httpOnly(true)
				.path("/")
				.secure(secure)
				.sameSite("None")
				.maxAge(60 * 60 * 24)
				.build();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE , accessCookie.toString())
				.header(HttpHeaders.SET_COOKIE , refreshCookie.toString())
				.body("Login Ok");
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
		String jti = jwtService.extractJti(refreshToken);
		if(!redisService.exists(jti)) {
			return ResponseEntity.status(401).body("Refresh token revogado");
		}
		
		//Verifica tempo máximo de sessão(milissegundos)
		long maxSessionTime = 1000 * 60 * 60 * 24;
		long createdAt = redisService.getCreatedAt(jti);
		if(System.currentTimeMillis() - createdAt > maxSessionTime) {
			redisService.delete(jti);
			return ResponseEntity.status(401).body("Sessão expirada");
		}
		
		//Rotação: invalida token antigo
		redisService.delete(jti);
		
		//Gera novos tokens
		String username = jwtService.extractUsername(refreshToken, "refresh_token");
		String newAccessToken = jwtService.generateAccessToken(username);
		String newRefreshToken = jwtService.generateRefreshToken(username);
		
		String newJti = jwtService.extractJti(newRefreshToken);
		
		//Salva novo refresh no Redis
		long ttl = 60 * 60 * 24;
		redisService.saveRefreshToken(newJti, username, createdAt, ttl);
		
		ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
				.httpOnly(true)
				.secure(secure)
				.sameSite("None")
				.path("/")
				.maxAge(60 * 10)
				.build();

		ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", newAccessToken)
				.httpOnly(true)
				.secure(secure)
				.sameSite("None")
				.path("/")
				.maxAge(60 * 60 * 24)
				.build();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, accessCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
				.body("Token Renovado");
	}
}
