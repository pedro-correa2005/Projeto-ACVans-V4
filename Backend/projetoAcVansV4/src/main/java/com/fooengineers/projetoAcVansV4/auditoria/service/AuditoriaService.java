package com.fooengineers.projetoAcVansV4.auditoria.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fooengineers.projetoAcVansV4.auditoria.dto.AuditoriaDTO;
import com.fooengineers.projetoAcVansV4.auditoria.dto.Detalhes;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Auditoria;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.auditoria.repository.AuditoriaRepository;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Usuario;
import com.fooengineers.projetoAcVansV4.util.IpUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditoriaService {
	@Autowired
	AuditoriaRepository auditoriaRepository;
	
	public Page<AuditoriaDTO> consultar(Oficina oficina, Pageable pageable) {
		return auditoriaRepository.findByOficinaOrderByTempoDesc(oficina, pageable).map(AuditoriaDTO::new);
	}
	
	public void registrar(Acao acao, Entidade entidade, Long idRegistro, Detalhes detalhes) {
		Auditoria auditoria = new Auditoria();
		auditoria.setAcao(acao);
		auditoria.setEntidade(entidade);
		auditoria.setIdRegistro(idRegistro);
		auditoria.setTempo(new Timestamp(System.currentTimeMillis()));
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) authentication.getPrincipal();
		
		auditoria.setEnderecoIp(IpUtil.getClientIp(request));
		auditoria.setDetalhes(detalhes);
		auditoria.setUsuario(usuario);
		if(usuario != null) auditoria.setOficina(usuario.getOficina());
		
		auditoriaRepository.save(auditoria);
	}
}
