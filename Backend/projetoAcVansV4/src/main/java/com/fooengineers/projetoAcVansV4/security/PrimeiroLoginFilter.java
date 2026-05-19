package com.fooengineers.projetoAcVansV4.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.service.UsuarioService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PrimeiroLoginFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UsuarioService usuarioService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	private static final List<String> ALLOWED_PATHS = List.of(
		"/api/auth/mudar-senha",
		"/api/auth/logout",
		"/api/auth/me"
	);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String path = request.getRequestURI();
		
		if(ALLOWED_PATHS.contains(path)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = jwtService.getTokenFromCookies(request, "access_token");
		
		if(token != null) {
			String email = jwtService.extrairUsername(token, "access_token");
			Usuario usuario = usuarioService.buscarPorEmail(email);
			if(usuario.isPrimeiroLogin()) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				Map<String, String> errorDetails = new HashMap<>();
	            errorDetails.put("code", "PASSWORD_CHANGE_REQUIRED");
	            errorDetails.put("message", "É necessário alterar a senha");

	            String jsonResponse = objectMapper.writeValueAsString(errorDetails);
	            response.getWriter().write(jsonResponse);
	            
	            return;
			}
		}
		filterChain.doFilter(request, response);
	}

}
