package com.fooengineers.projetoAcVansV4.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.auditoria.dto.Detalhes;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.auditoria.formatter.AuditoriaFormatter;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.dto.TipoServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.TipoServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.exception.TipoServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.TipoServicoRepository;

@Service
public class TipoServicoService {
	@Autowired
	TipoServicoRepository tipoServicoRepository;
	@Autowired
	private AuditoriaService auditoriaService;
	
	public List<TipoServicoResDTO> listar(Oficina oficina){
		return tipoServicoRepository.findByOficina(oficina).stream()
				.map(TipoServicoResDTO::new)
				.collect(Collectors.toList());
	}
	
	public TipoServicoResDTO criar(TipoServicoReqDTO dto, Oficina oficina) {
		TipoServico tipo = new TipoServico();
		tipo.setDescricao(dto.getDescricao());
		tipo.setOficina(oficina);
		TipoServico criado = tipoServicoRepository.save(tipo);
		return new TipoServicoResDTO(criado);
	}
	
	public TipoServicoResDTO atualizar(TipoServicoReqDTO dto, Long idTipoServico) {
		TipoServico tipo = tipoServicoRepository.findById(idTipoServico).orElseThrow(() -> new TipoServicoNaoEncontradoException(idTipoServico));
		
		Map<String, Object> antes = AuditoriaFormatter.formatarTipo(tipo);

		
		tipo.setDescricao(dto.getDescricao());
		TipoServico atualizado = tipoServicoRepository.save(tipo);
		
		Map<String, Object> depois = AuditoriaFormatter.formatarTipo(atualizado);
		
		Map<String, Object> alteracoes = new LinkedHashMap<>();
		alteracoes.put("antes", antes);
		alteracoes.put("depois", depois);
		Detalhes detalhes = new Detalhes(alteracoes);
		
		auditoriaService.registrar(
				Acao.UPDATE,
				Entidade.TIPO_SERVICO,
				idTipoServico,
				detalhes);
		
		return new TipoServicoResDTO(atualizado);
	}
	
	public void deletar(Long idTipoServico) {
		TipoServico tipo = tipoServicoRepository.findById(idTipoServico).orElseThrow(() -> new TipoServicoNaoEncontradoException(idTipoServico));
		tipoServicoRepository.delete(tipo);
	}
}
