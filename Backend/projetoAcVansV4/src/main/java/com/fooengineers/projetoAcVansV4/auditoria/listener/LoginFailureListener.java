package com.fooengineers.projetoAcVansV4.auditoria.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginFailureListener {
	@Autowired
	private AuditoriaService auditoriaService;

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        auditoriaService.registrar(
                Acao.LOGIN_FAIL,
                null,
                null,
                null
        );
    }
}
