package com.fooengineers.projetoAcVansV4.auditoria.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessListener {
	@Autowired
	private AuditoriaService auditoriaService;
	
	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {
		
		auditoriaService.registrarLogin(event.getAuthentication());
	}
}
