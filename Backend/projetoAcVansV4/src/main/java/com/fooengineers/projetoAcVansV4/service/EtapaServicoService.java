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
import com.fooengineers.projetoAcVansV4.dto.EtapaServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.EtapaServicoResDTO;
import com.fooengineers.projetoAcVansV4.dto.OrdemDTO;
import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.StatusServico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.exception.EtapaServicoNaoEncontradaException;
import com.fooengineers.projetoAcVansV4.exception.ReordenacaoNaoPermitidaException;
import com.fooengineers.projetoAcVansV4.exception.StatusServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.exception.TipoServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.EtapaServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.ServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.StatusServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.TipoServicoRepository;

@Service
public class EtapaServicoService {
	@Autowired
	private EtapaServicoRepository etapaServicoRepository;
	@Autowired
	private TipoServicoRepository tipoServicoRepository;
	@Autowired
	private ServicoRepository servicoRepository;
	@Autowired
	private StatusServicoRepository statusServicoRepository;
	@Autowired
	private AuditoriaService auditoriaService;
	
	public List<EtapaServicoResDTO> listarPorTipo(TipoServico tipo){
		return etapaServicoRepository.findByTipoServico(tipo)
				.stream().map(EtapaServicoResDTO::new)
				.collect(Collectors.toList());
	}
	public List<EtapaServicoResDTO> listarPorTipo(Long idTipoServico){
		TipoServico tipo = tipoServicoRepository.findById(idTipoServico).orElseThrow(() -> new TipoServicoNaoEncontradoException(idTipoServico));
		return etapaServicoRepository.findByTipoServico(tipo)
				.stream().map(EtapaServicoResDTO::new)
				.collect(Collectors.toList());
	}
	
	public EtapaServicoResDTO criar(EtapaServicoReqDTO dto, Long idTipoServico) {
		TipoServico tipo = tipoServicoRepository.findById(idTipoServico).orElseThrow(() -> new TipoServicoNaoEncontradoException(idTipoServico));
		EtapaServico etapa = new EtapaServico();
		EtapaServico ultimaEtapa = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemDesc(tipo).orElse(null);
		int ordem = 100;
		if(ultimaEtapa != null) {
			ordem = ultimaEtapa.getOrdem() + 100;
		}
		etapa.setTitulo(dto.getTitulo());
		etapa.setDescricao(dto.getDescricao());
		etapa.setOrdem(ordem);
		etapa.setTipoServico(tipo);
		etapa.setOficina(tipo.getOficina());
		
		EtapaServico criado = etapaServicoRepository.save(etapa);
		
		return new EtapaServicoResDTO(criado);
	}
	
	public EtapaServicoResDTO atualizar(EtapaServicoReqDTO dto, Long idEtapaServico) {
		EtapaServico etapa = etapaServicoRepository.findById(idEtapaServico).orElseThrow(() -> new EtapaServicoNaoEncontradaException(idEtapaServico));
		
		Map<String, Object> antes = AuditoriaFormatter.formatarEtapa(etapa);
		
		etapa.setTitulo(dto.getTitulo());
		etapa.setDescricao(dto.getDescricao());
		EtapaServico atualizado = etapaServicoRepository.save(etapa);
		
		Map<String, Object> depois = AuditoriaFormatter.formatarEtapa(atualizado);
		
		Map<String, Object> alteracoes = new LinkedHashMap<>();
		alteracoes.put("antes", antes);
		alteracoes.put("depois", depois);
		Detalhes detalhes = new Detalhes(alteracoes);
		auditoriaService.registrar(
				Acao.UPDATE,
				Entidade.ETAPA_SERVICO,
				idEtapaServico,
				detalhes);
		
		return new EtapaServicoResDTO(atualizado);
	}
	
	public EtapaServicoResDTO atualizarOrdem(OrdemDTO dto, Long idEtapaServico) {
		EtapaServico etapa = etapaServicoRepository.findById(idEtapaServico).orElseThrow(() -> new EtapaServicoNaoEncontradaException(idEtapaServico));
		TipoServico tipo = etapa.getTipoServico();
		StatusServico status = statusServicoRepository.findByDescricao("INICIADO").orElseThrow(() -> new StatusServicoNaoEncontradoException("INICIADO")); 
		int numServicos = servicoRepository.countByTipoServicoAndStatusServico(tipo, status);
		if(numServicos > 0) {
			throw new ReordenacaoNaoPermitidaException();
		}
		Map<String, Object> antes = AuditoriaFormatter.formatarEtapa(etapa);
		
		Integer novaOrdem;
		if(dto.getOrdemAnterior() == null) {
			novaOrdem = (dto.getOrdemProxima() - 100);
		}else if(dto.getOrdemProxima() == null) {
			novaOrdem = (dto.getOrdemAnterior() + 100);
		}else {
			novaOrdem = ((dto.getOrdemAnterior() + dto.getOrdemProxima()) / 2);
		}
		
		if(novaOrdem.equals(dto.getOrdemAnterior()) || novaOrdem.equals(dto.getOrdemProxima())) {
			novaOrdem = reindexar(etapa);
		}
		
		etapa.setOrdem(novaOrdem);
		
		EtapaServico atualizada = etapaServicoRepository.save(etapa);
		
		Map<String, Object> depois = AuditoriaFormatter.formatarEtapa(atualizada);
		
		Map<String, Object> alteracoes = new LinkedHashMap<>();
		alteracoes.put("antes", antes);
		alteracoes.put("depois", depois);
		
		Detalhes detalhes = new Detalhes(alteracoes);
		auditoriaService.registrar(
				Acao.UPDATE,
				Entidade.ETAPA_SERVICO,
				idEtapaServico,
				detalhes);
		return new EtapaServicoResDTO(atualizada);
	}
	
	private Integer reindexar(EtapaServico etapa) {
		List<EtapaServico> etapas = etapaServicoRepository.findByTipoServico(etapa.getTipoServico());
		
		int ordem = 100;
		for(EtapaServico e: etapas) {
			EtapaServicoReqDTO dto = new EtapaServicoReqDTO();
			dto.setTitulo(e.getTitulo());
			dto.setDescricao(e.getDescricao());
			e.setOrdem(ordem);
			atualizar(dto, e.getId());
			ordem += 100;
		}
		return etapaServicoRepository.findById(etapa.getId()).get().getOrdem();
	}
	
	public void deletar(Long idEtapaServico) {
		EtapaServico etapa = etapaServicoRepository.findById(idEtapaServico).orElseThrow(() -> new EtapaServicoNaoEncontradaException(idEtapaServico));		
		etapaServicoRepository.delete(etapa);
	}
}
