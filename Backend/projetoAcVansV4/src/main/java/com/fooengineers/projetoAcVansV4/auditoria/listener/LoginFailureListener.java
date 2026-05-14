package com.fooengineers.projetoAcVansV4.auditoria.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.repository.UsuarioRepository;
import com.fooengineers.projetoAcVansV4.util.IpUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginFailureListener {
	@Autowired
	private AuditoriaService auditoriaService;
	@Autowired
    private UsuarioRepository usuarioRepository;

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {

        String email = event.getAuthentication()
                .getName();

        Usuario usuario =
                usuarioRepository
                        .findByEmail(email)
                        .orElse(null);
        
        Oficina oficina = null;
        
        if(usuario != null) oficina = usuario.getOficina();
        
        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder
                                .getRequestAttributes())
                        .getRequest();

        String ip = IpUtil.getClientIp(request);

        auditoriaService.registrar(
                Acao.LOGIN_FAIL,
                null,
                null,
                ip,
                null,
                usuario,
                oficina
        );
    }
}
