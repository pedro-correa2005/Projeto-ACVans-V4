package com.fooengineers.projetoAcVansV4.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fooengineers.projetoAcVansV4.entity.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService; 
	@Autowired
	private CustomUserDetailsService userDetailsService; 
	@Override
	
	//Filtro de requisições
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtService.getTokenFromCookies(request, "access_token"); //Pega token do cookie enviado pela request
		
		if(token != null) {
			try {
				//Extrai usuário do token
				String username = jwtService.extractUsername(token, "access_token");
				
				//Se já está autenticado, não refaz
				if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(username);
					if(jwtService.isValid(token, username, "access_token")) {
						UsernamePasswordAuthenticationToken auth = 
								new UsernamePasswordAuthenticationToken(
										usuario,
										null,
										usuario.getAuthorities()
						);
						auth.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request)
						);
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
				}
			} catch (Exception e) {
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);
	}
}
