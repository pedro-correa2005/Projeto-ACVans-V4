package com.fooengineers.projetoAcVansV4.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
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
	
	/*
	 * A chave é extraída de application.properties, que por sua vez é extraída
	 * de variável de ambiente
	 * A chave é transformada de String para bytes criptografados
	 */
	public JwtService(@Value("${jwt.access.secret}") String secretAccessKey, @Value("${jwt.refresh.secret}") String secretRefreshKey) {
		this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccessKey));;
		this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRefreshKey));;
	}
	
	// Método de geração de token de acesso
	public String generateAccessToken(String username) {
		return Jwts.builder()
			.subject(username) //Insere nome de usuário no token
			.issuedAt(new Date()) //Momento de ciração do token
			.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) //Seta expiração de 10 minutos
			.signWith(accessKey) //Insere chave JWT que valida o token
			.compact();
	}
	
	// Método de geração de token de refresh (mesmo conceito, duração maior, expira em um dia) 
	public String generateRefreshToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		
		String jti = UUID.randomUUID().toString();
		
		claims.put("jti", jti);
		claims.put("type", "refresh");
		
		long now = System.currentTimeMillis();
		long expiration = now + (1000 * 60 * 60 * 24);
		
		return Jwts.builder()
			.claims(claims)
			.subject(username)
			.issuedAt(new Date(now))
			.expiration(new Date(expiration))
			.signWith(refreshKey)
			.compact();
	}
	
	// Método de extração de nome de usuário do token
	public String extractUsername(String token, String tokenType) {
		SecretKey key = tokenType.equals("access_token") ? accessKey : refreshKey; 
	    return Jwts.parser()
	        .verifyWith(key) // Verifica se o token é válido pela chave
	        .build()
	        .parseSignedClaims(token) // Lê o jwt completo
	        .getPayload()
	        .getSubject(); //Extrai usuário
	}
	
	//Método para extrair o identificador único do refresh token
	public String extractJti(String token) {
		return extractAllClaims(token, "refresh_token").get("jti", String.class);
	}
	
	//Método para extrair claims do token
	public Claims extractAllClaims(String token, String tokenType) {
		SecretKey key = tokenType.equals("access_token") ? accessKey : refreshKey;
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
	
	// Método de validação do token
	public boolean isValid(String token, String username, String tokenType) {
		//Extrai o username do token
		String extractedUsername = extractUsername(token, tokenType);
		//Verifica se token pertence ao usuário e não está expirado
		return extractedUsername.equals(username) && !isExpired(token, tokenType);
	}
	
	//Método de verificação de expiração do token
	public boolean isExpired(String token, String tokenType) {
		Date expiration = extractAllClaims(token, tokenType).getExpiration();
		return expiration.before(new Date()); //Verifica se a data de expiração já passou
	}

	public String getTokenFromCookies(HttpServletRequest request, String tokenType) {
		if (request.getCookies() == null) return null;
		
		for (Cookie cookie : request.getCookies()) {
			if(tokenType.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
}