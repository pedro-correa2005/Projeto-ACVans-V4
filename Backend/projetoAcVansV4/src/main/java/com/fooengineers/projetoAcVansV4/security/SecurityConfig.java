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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtFilter;
	@Autowired
	private CsrfFilter csrfFilter;
	@Autowired
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Value("${frontend.ip}") 
	private String frontendIp;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		return http
				.cors(cors -> cors.configurationSource(
						cosrConfigurationSource()
				))
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
							"/api/auth/redefinir-senha/validar-token",
							"/api/auth/logout",
							"/api/consulta",
							"/api/atualizar-etapa/**",
							"/v3/api-docs/**",
						    "/swagger-ui/**",
						    "/swagger-ui.html").permitAll()
					.requestMatchers("/api/admin/**").hasRole("ADMIN")
					.requestMatchers(
							"/api/relatorios/**",
							"/api/auditoria",
							"/api/tipos-servico",
							"/api/tipos-servico/**",
							"/api/etapas-servico/**",
							"/api/etapas-servico"
							).hasRole("GERENTE")
					//Qualquer outro endpoint
					.anyRequest().authenticated()
				)
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint(jwtAuthenticationEntryPoint)
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
	public CorsConfigurationSource cosrConfigurationSource() {
		CorsConfiguration config =
                new CorsConfiguration();

        config.setAllowedOrigins(
                List.of(frontendIp)
        );

        config.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH"
                )
        );

        config.setAllowedHeaders(
                List.of("*")
        );

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource
                source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config
        );

        return source;
	}
	
