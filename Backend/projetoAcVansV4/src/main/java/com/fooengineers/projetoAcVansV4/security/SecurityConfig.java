package com.fooengineers.projetoAcVansV4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.repository.UsuarioRepository;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtFilter;
	@Autowired
	private CsrfFilter csrfFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		return http
				//Desativa csrf
				.csrf(csrf -> csrf.disable())
				//Stateless
				.sessionManagement(session ->
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				//Regras de acesso
				.authorizeHttpRequests(auth -> auth
					//endpoints públicos
					.requestMatchers(
							"/api/",
							"/api/auth/login", 
							"/api/auth/refresh",
							"/api/auth/2fa/verificar",
							"/api/auth/esqueci-a-senha",
							"/api/auth/redefinir-senha",
							"/v3/api-docs/**",
						    "/swagger-ui/**",
						    "/swagger-ui.html").permitAll()
					//Qualquer outr endpoint
					.anyRequest().authenticated()
				)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(csrfFilter, JwtAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configs) throws Exception{
		return configs.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Value("${frontend.ip}") 
			String frontendIp;
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(frontendIp)
						.allowedMethods("*")
						.allowCredentials(true);
			}
		};
	}
	
