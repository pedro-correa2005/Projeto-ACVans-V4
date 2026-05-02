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
import com.fooengineers.projetoAcVansV4.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDto){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDto.getEmail(),
						loginDto.getSenha()
						)
				);
		String accessToken = jwtService.generateAccessToken(loginDto.getEmail());
		
		ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
				.httpOnly(true)
				.path("/")
				.maxAge(60 * 10)
				.build();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE , cookie.toString())
				.body("Login Ok");
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request){
		String refreshToken = jwtService.getTokenFromCookies(request, "refresh_token");
		
		if(refreshToken == null) {
			return ResponseEntity.status(401).body("Refresh token ausente");
		}
		
		String username = jwtService.extractUsername(refreshToken);
		
		if(!jwtService.isValid(refreshToken, username)) {
			return ResponseEntity.status(401).body("Refresh token inválido");
		}
		
		String newAccessToken = jwtService.generateAccessToken(username);
		
		ResponseCookie cookie = ResponseCookie.from("access_token", newAccessToken)
				.httpOnly(true)
				.path("/")
				.maxAge(60 * 10)
				.build();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body("Token Renovado");
	}
}
