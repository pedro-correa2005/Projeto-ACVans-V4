package com.fooengineers.projetoAcVansV4.auditoria.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.auditoria.dto.Detalhes;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Auditoria;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.auditoria.repository.AuditoriaRepository;
import com.fooengineers.projetoAcVansV4.dto.AuditoriaDTO;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Usuario;

@Service
public class AuditoriaService {
	@Autowired
	AuditoriaRepository auditoriaRepository;
	
	public Page<AuditoriaDTO> consultar(Oficina oficina, Pageable pageable) {
		return auditoriaRepository.findByOficinaOrderByTempoDesc(oficina, pageable).map(AuditoriaDTO::new);
	}
	
	public void registrar(Acao acao, Entidade entidade, Long idRegistro, String enderecoIp, Detalhes detalhes, Usuario usuario, Oficina oficina) {
		Auditoria auditoria = new Auditoria();
		auditoria.setAcao(acao);
		auditoria.setEntidade(entidade);
		auditoria.setIdRegistro(idRegistro);
		auditoria.setTempo(new Timestamp(System.currentTimeMillis()));
		auditoria.setEnderecoIp(enderecoIp);
		auditoria.setDetalhes(detalhes);
		auditoria.setUsuario(usuario);
		auditoria.setOficina(oficina);
		auditoriaRepository.save(auditoria);
	}
}
