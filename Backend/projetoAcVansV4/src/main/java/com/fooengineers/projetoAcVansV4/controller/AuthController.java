package com.fooengineers.projetoAcVansV4.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooengineers.projetoAcVansV4.dto.EmailDTO;
import com.fooengineers.projetoAcVansV4.dto.LoginRequestDTO;
import com.fooengineers.projetoAcVansV4.dto.MudarSenhaDTO;
import com.fooengineers.projetoAcVansV4.dto.RedefinirSenhaDTO;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.security.JwtService;
import com.fooengineers.projetoAcVansV4.security.MFAService;
import com.fooengineers.projetoAcVansV4.service.AuthService;
import com.fooengineers.projetoAcVansV4.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private MFAService mfaService;
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginDto, HttpServletRequest request){
		//Verifica número de tentativas de login
		String email = loginDto.getEmail().toLowerCase();
		
		if(!authService.validarTentativas(request.getRemoteAddr(), email)) {
			return ResponseEntity.status(429).body("Muitas tentativas. Tente mais tarde.");
		}
		
		Usuario usuario = authService.validarCredenciais(email, loginDto.getSenha());
		
		if(usuario == null) {
			return ResponseEntity.status(401).body("Email ou senha incorretos");
		}
		
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
		
		//Salva o código e gera token temporário
		String code = mfaService.gerarCodigo();
		String tempToken = mfaService.salvarCodigo(email, code);
		
		//Envia código no email
		mfaService.enviarEmail(email, code);
		
		Map<String, String> body = new HashMap<>();
		body.put("status", "2FA_required");
		body.put("tempToken", tempToken);
		
		return ResponseEntity.status(202).body(body);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(HttpServletRequest request){
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
		if(!jwtService.validarRedis(refreshToken)) {
			return ResponseEntity.status(401).body("Refresh token revogado ou expirado");
		}
		
		String username = jwtService.extrairUsername(refreshToken, "refresh_token");
		long createdAt = jwtService.getCreatedAt(refreshToken);

		//Rotação: invalida token antigo
		jwtService.deletarToken(refreshToken);
		
		//Cria novos cookies
		ResponseCookie accessCookie = jwtService.gerarAccessCookie(username);
		ResponseCookie refreshCookie = jwtService.gerarRefreshCookie(username, createdAt); //Tempo de criação do token anterior é repassado
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, accessCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
				.body("Token Renovado");
	}
	
	@PostMapping("/2fa/verificar")
	public ResponseEntity<?> verificar(@RequestBody Map<String, String> body){
		String tempToken = body.get("tempToken");
		String code = body.get("code");
		String email;

		//Verifica token
		if(tempToken == null || (email = mfaService.verificarTempToken(tempToken)) == null) {
			return ResponseEntity.status(401).body("Sessão inválida");
		}
		//Incrementa e valida número de tentativas
		if(!mfaService.verificarAttempts(tempToken)) {
			mfaService.deletar2FA(email, tempToken);//Se expirado deleta tudo e exige novo login
			return ResponseEntity.status(429).body("Número de tentativas expirado");
		}
		//Verifica código
		if(!mfaService.verificarCodigo(email, code)) {
			return ResponseEntity.status(401).body("Código inválido ou expirado");
		}
		//Código válido
		mfaService.deletar2FA(email, tempToken);
		//Gera JWT
		ResponseCookie accessCookie = jwtService.gerarAccessCookie(email);
		ResponseCookie refreshCookie = jwtService.gerarRefreshCookie(email, System.currentTimeMillis());
		ResponseCookie csrfCookie =  jwtService.gerarCsrfCookie();
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE , accessCookie.toString())
				.header(HttpHeaders.SET_COOKIE , refreshCookie.toString())
				.header(HttpHeaders.SET_COOKIE , csrfCookie.toString())
				.body("Login Ok");
	}
	
	@PostMapping("/mudar-senha")
	public ResponseEntity<?> mudarSenha(@RequestBody @Valid MudarSenhaDTO dto, Authentication authentication, HttpServletRequest request){
		String email = authentication.getName();
		String senhaAtual = dto.getSenhaAtual();
		String novaSenha = dto.getNovaSenha();
		String repetirNovaSenha = dto.getRepetirNovaSenha();
		
		Usuario usuario = authService.validarCredenciais(email, senhaAtual);
		if(usuario == null) {
			return ResponseEntity.status(401).body("Senha atual incorreta.");
		}
		
		if(!novaSenha.equals(repetirNovaSenha)) {
			return ResponseEntity.badRequest().body("As senhas não coincidem.");
		}
		
		List<String> errors = authService.validarNovaSenha(novaSenha, usuario);
		if(!(errors.isEmpty())) {
			return ResponseEntity.badRequest().body(errors);
		}
		usuarioService.alterarSenha(usuario, novaSenha);
		String refreshToken = jwtService.getTokenFromCookies(request, "refresh_token");
		jwtService.deletarToken(refreshToken);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE , jwtService.deletarCookie("access_token").toString())
				.header(HttpHeaders.SET_COOKIE , jwtService.deletarCookie("refresh_token").toString())
				.header(HttpHeaders.SET_COOKIE , jwtService.deletarCookie("csrf_token").toString())
				.body("Senha alterada com sucesso.");
	}
	
	@PostMapping("/esqueci-a-senha")
	public ResponseEntity<?> esqueciSenha(@RequestBody EmailDTO dto){
		authService.esqueciSenha(dto.getEmail());
		return ResponseEntity.ok("Se o email existir, enviaremos instruções de recuperação");
	}
	
	@PostMapping("/redefinir-senha")
	public ResponseEntity<?> redefinirSenha(@RequestBody @Valid RedefinirSenhaDTO dto){
		String token = dto.getToken();
		String novaSenha = dto.getNovaSenha();
		String repetirNovaSenha = dto.getRepetirNovaSenha();
		
		Usuario usuario = authService.validarResetToken(token);
		if(usuario == null) {
			return ResponseEntity.status(401).body("Token inválido.");
		}
		
		if(!novaSenha.equals(repetirNovaSenha)) {
			return ResponseEntity.badRequest().body("As senhas não coincidem.");
		}
		
		List<String> errors = authService.validarNovaSenha(novaSenha, usuario);
		if(!(errors.isEmpty())) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		usuarioService.alterarSenha(usuario, novaSenha);
		return ResponseEntity.ok("Senha alterada com sucesso.");
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String refreshToken = jwtService.getTokenFromCookies(request, "refresh_token");
		
		if(refreshToken != null) {
			jwtService.deletarToken(refreshToken);
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE , jwtService.deletarCookie("access_token").toString())
				.header(HttpHeaders.SET_COOKIE , jwtService.deletarCookie("refresh_token").toString())
				.header(HttpHeaders.SET_COOKIE , jwtService.deletarCookie("csrf_token").toString())
				.body("Você saiu da sua conta.");
	}
}
