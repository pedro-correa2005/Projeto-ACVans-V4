package com.fooengineers.projetoAcVansV4.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CsrfFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getMethod().equals("GET")) {
		    filterChain.doFilter(request, response);
		    return;
		}
		if(getCookieValue(request, "access_token") == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String csrfHeader = request.getHeader("X-CSRF-TOKEN");
		String csrfCookie = getCookieValue(request, "csrf_token");
		if (csrfHeader == null || csrfCookie == null || !csrfHeader.equals(csrfCookie)) {
		    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		    response.getWriter().write("CSRF inválido");
		    return;
		}
		filterChain.doFilter(request, response);
	}
	
	private String getCookieValue(HttpServletRequest request, String name) {
		if (request.getCookies() == null) return null;
		
		for (Cookie cookie : request.getCookies()) {
		    if (cookie.getName().equals(name)) {
		        return cookie.getValue();
		    }
		}
		return null;
	}

}
