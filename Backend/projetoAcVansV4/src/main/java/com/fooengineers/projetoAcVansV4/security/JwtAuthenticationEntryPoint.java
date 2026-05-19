package com.fooengineers.projetoAcVansV4.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fooengineers.projetoAcVansV4.exception.dto.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	JwtService jwtService;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		String token = jwtService.getTokenFromCookies(request, "refresh_token");
		String error = "Unauthorized";
		if(token != null && jwtService.isValid(token, "refresh_token")) {
			error = "TOKEN_EXPIRED";
		}
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        

        ErrorResponse errorBody = new ErrorResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                error,
                "Token inválido, expirado ou ausente.",
                request.getRequestURI()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
	}

}
