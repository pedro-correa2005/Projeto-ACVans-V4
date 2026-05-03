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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	
	@Value("${app.security.cookie.secure}")
	private boolean secure;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDto){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDto.getEmail(),
						loginDto.getSenha()
						)
				);
		String email = loginDto.getEmail();
		
		String accessToken = jwtService.generateAccessToken(email);
		String refreshToken = jwtService.generateRefreshToken(email);
		
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
		String refreshToken = jwtService.getTokenFromCookies(request, "refresh_token");
		
		if(refreshToken == null) {
			return ResponseEntity.status(401).body("Refresh token ausente");
		}
		
		String username = jwtService.extractUsername(refreshToken, "refresh_token");
		
		if(!jwtService.isValid(refreshToken, username, "refresh_token")) {
			return ResponseEntity.status(401).body("Refresh token inválido");
		}
		
		String newAccessToken = jwtService.generateAccessToken(username);
		
		ResponseCookie cookie = ResponseCookie.from("access_token", newAccessToken)
				.httpOnly(true)
				.secure(secure)
				.sameSite("None")
				.path("/")
				.maxAge(60 * 10)
				.build();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body("Token Renovado");
	}
}
