package com.fooengineers.projetoAcVansV4.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.service.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
/*
 * Service para geração e validação de tokens JWT
 * extração de informações do token e checagem de expiração
 */
@Service
public class JwtService {
	
	//Chave de codificação do JWT (acesso)
	private final SecretKey accessKey;
	//Chave de codificação do JWT (refresh)
	private final SecretKey refreshKey;
	private final boolean secure;
	private final long accessTime;
	private final long refreshTime;
	
	@Autowired
	private RedisService redisService;
	
	
	/*
	 * A chave é extraída de application.properties, que por sua vez é extraída
	 * de variável de ambiente
	 * A chave é transformada de String para bytes criptografados
	 */
	public JwtService(@Value("${jwt.access.secret}") String secretAccessKey, @Value("${jwt.refresh.secret}") String secretRefreshKey, @Value("${app.security.cookie.secure}") boolean secure) {
		this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccessKey));
		this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRefreshKey));
		this.secure = secure;
		accessTime = 60 * 10;
		refreshTime = 60 * 60 * 24;
	}
	
	// Método de geração de token de acesso
	public String gerarAccessToken(String username) {
		return Jwts.builder()
			.subject(username) //Insere nome de usuário no token
			.issuedAt(new Date()) //Momento de ciração do token
			.expiration(new Date(System.currentTimeMillis() + 1000 * accessTime)) //Seta expiração de 10 minutos
			.signWith(accessKey) //Insere chave JWT que valida o token
			.compact();
	}
	
	// Método de geração de token de refresh (mesmo conceito, duração maior, expira em um dia) 
	public String gerarRefreshToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		
		String jti = UUID.randomUUID().toString();
		
		claims.put("jti", jti);
		claims.put("type", "refresh");
		
		long now = System.currentTimeMillis();
		long expiration = now + (1000 * refreshTime);
		
		return Jwts.builder()
			.claims(claims)
			.subject(username)
			.issuedAt(new Date(now))
			.expiration(new Date(expiration))
			.signWith(refreshKey)
			.compact();
	}
	
	// Método de extração de nome de usuário do token
	public String extrairUsername(String token, String tokenType) {
		SecretKey key = tokenType.equals("access_token") ? accessKey : refreshKey; 
	    return Jwts.parser()
	        .verifyWith(key) // Verifica se o token é válido pela chave
	        .build()
	        .parseSignedClaims(token) // Lê o jwt completo
	        .getPayload()
	        .getSubject(); //Extrai usuário
	}
	
	//Método para extrair o identificador único do refresh token
	public String extrairJti(String token) {
		return extrairAllClaims(token, "refresh_token").get("jti", String.class);
	}
	
	//Método para extrair claims do token
	public Claims extrairAllClaims(String token, String tokenType) {
		SecretKey key = tokenType.equals("access_token") ? accessKey : refreshKey;
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
	
	// Método de validação do token verifica se é válido e se já não está expirado;
	public boolean isValid(String token, String tokenType) {
	    try {
	        SecretKey key = tokenType.equals("access_token") ? accessKey : refreshKey;
	        //Parser verifica automaticamente a validez
	        Jwts.parser()
	            .verifyWith(key)
	            .build()
	            .parseSignedClaims(token);
	        return true;
	        //Se extiver expirado lança exceção
	    } catch (ExpiredJwtException e) {
	        return false; // expirado
	        //Se chave for inválida lança exceção
	    } catch (JwtException | IllegalArgumentException e) {
	        return false; // inválido
	    }
	}
	
	//Método de extrair token do cookie
	public String getTokenFromCookies(HttpServletRequest request, String tokenType) {
		if (request.getCookies() == null) return null;
		
		for (Cookie cookie : request.getCookies()) {
			if(tokenType.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	//Método de geração de cookie de acesso
	public ResponseCookie gerarAccessCookie(String email) {
		String accessToken = gerarAccessToken(email);
		return ResponseCookie.from("access_token", accessToken)
				.httpOnly(true)
				.secure(secure)
				.sameSite("None")
				.path("/")
				.maxAge(accessTime)
				.build();
	}
	
	//Método de geração de cooke de refresh
	public ResponseCookie gerarRefreshCookie(String email, Long createdAt) {
		//Gera token
		String refreshToken = gerarRefreshToken(email);
		
		//Salva no redis o identificador único jti:email com tempo de expiração igual ao token
		String jti = extrairJti(refreshToken);
		long ttl = 60 * 60 * 24;
		redisService.saveRefreshToken(jti, email, createdAt, ttl);
		
		return ResponseCookie.from("refresh_token", refreshToken)
				.httpOnly(true)
				.path("/")
				.secure(secure)
				.sameSite("None")
				.maxAge(refreshTime)
				.build();
	}
	public ResponseCookie gerarCsrfCookie() {
		String csrfToken = UUID.randomUUID().toString();
		return ResponseCookie.from("csrf_token", csrfToken)
				.httpOnly(false)
				.path("/")
				.secure(secure)
				.sameSite("None")
				.maxAge(refreshTime)
				.build();
	}
	
	public boolean validarRedis(String refreshToken) {
		//Verifica se o token existe no redis
		String jti = extrairJti(refreshToken);
		if(!redisService.exists(jti)) {
			return false;
		}
		
		//Verifica tempo máximo de sessão(milissegundos)
		long maxSessionTime = 1000 * refreshTime;
		long createdAt = redisService.getCreatedAt(jti);
		if(System.currentTimeMillis() - createdAt > maxSessionTime) {
			redisService.delete(jti);
			return false;
		}
		
		return true;
	}
	
	public void deletarToken(String refreshToken) {
		String jti = extrairJti(refreshToken);
		redisService.delete(jti);
	}
	
	public long getCreatedAt(String refreshToken) {
		String jti = extrairJti(refreshToken);
		return redisService.getCreatedAt(jti);
	}
	
	public ResponseCookie deletarCookie(String token) {
		return ResponseCookie.from(token, "")
				.httpOnly(true)
				.secure(secure)
				.sameSite("None")
				.path("/")
				.maxAge(0)
				.build();
	}
}