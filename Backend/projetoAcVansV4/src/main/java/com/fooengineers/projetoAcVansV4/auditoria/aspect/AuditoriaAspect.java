package com.fooengineers.projetoAcVansV4.auditoria.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fooengineers.projetoAcVansV4.auditoria.annotation.Auditavel;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.auditoria.support.EntidadeAuditavel;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {
	private final AuditoriaService auditoriaService;
	
	@AfterReturning(
		value = "@annotation(auditavel)",
		returning = "resultado"
	)
	public void auditar(JoinPoint joinPoint, Auditavel auditavel, Object resultado) {
		Long idRegistro = extrairId(resultado);
		
		auditoriaService.registrar(
				auditavel.acao(),
				auditavel.entidade(),
				idRegistro,
				null
		);
	}
	
	private Long extrairId(Object resultado) {
		if(resultado instanceof EntidadeAuditavel auditavel) {
			return auditavel.getId();
		}
		
		return null;
	}
}
