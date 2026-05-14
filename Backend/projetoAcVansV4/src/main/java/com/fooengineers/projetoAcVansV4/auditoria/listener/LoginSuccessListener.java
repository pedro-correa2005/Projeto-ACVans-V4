package com.fooengineers.projetoAcVansV4.auditoria.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.util.IpUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessListener {
	@Autowired
	private AuditoriaService auditoriaService;
	
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {
		Usuario usuario = (Usuario) event.getAuthentication().getPrincipal();
		
		HttpServletRequest request = ((ServletRequestAttributes)
				RequestContextHolder.getRequestAttributes())
				.getRequest();
		
		String ip = IpUtil.getClientIp(request);
		
		auditoriaService.registrar(
				Acao.LOGIN,
				null,
				null,
				ip,
				null, 
				usuario,
				usuario.getOficina());
	}
}
