package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.auditoria.formatter.AuditoriaFormatter;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.dto.CadastroResDTO;
import com.fooengineers.projetoAcVansV4.dto.EtapaServicoResDTO;
import com.fooengineers.projetoAcVansV4.dto.ServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.ServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.entity.StatusServico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.entity.Veiculo;
import com.fooengineers.projetoAcVansV4.exception.ErroAoGerarQrCode;
import com.fooengineers.projetoAcVansV4.exception.EtapaServicoNaoEncontradaException;
import com.fooengineers.projetoAcVansV4.exception.OficinaDesativadaException;
import com.fooengineers.projetoAcVansV4.exception.OficinaNaoEncontradaException;
import com.fooengineers.projetoAcVansV4.exception.OrdemEtapaNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.exception.ServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.exception.StatusServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.exception.TipoServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.exception.VeiculoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.EtapaServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.OficinaRepository;
import com.fooengineers.projetoAcVansV4.repository.ServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.StatusServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.TipoServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.VeiculoRepository;
import com.fooengineers.projetoAcVansV4.specification.ServicoSpecification;
import com.fooengineers.projetoAcVansV4.util.QrCodeUtil;
import com.fooengineers.projetoAcVansV4.util.TokenUtil;
import com.google.zxing.WriterException;

@Service
public class ServicoService {
	@Autowired
	private ServicoRepository servicoRepository;
	@Autowired
	private TipoServicoRepository tipoServicoRepository;
	@Autowired
	private StatusServicoRepository statusServicoRepository;
	@Autowired
	private EtapaServicoRepository etapaServicoRepository;
	@Autowired
	private OficinaRepository oficinaRepository;
	@Autowired
	private VeiculoRepository veiculoRepository;
	@Autowired
	private HistoricoEtapaService historicoEtapaService;
	@Value("${app.base-url}")
	private String baseUrl;
	
	@Autowired
	private AuditoriaService auditoriaService;

	public Page<ServicoResDTO> listar(Oficina oficina, Pageable pageable) {
		return servicoRepository.findByOficina(oficina, pageable).map(ServicoResDTO::new);
	}
	
	public Page<ServicoResDTO> buscarPorVeiculo(Long idVeiculo, String termo, Pageable pageable){
		return servicoRepository.findAll(ServicoSpecification.filtrarPorVeiculo(termo, idVeiculo), pageable).map(ServicoResDTO::new);
	}
	
	public ServicoResDTO criar(ServicoReqDTO dto, Integer idOficina) {
		Oficina oficina = oficinaRepository.findById(idOficina).orElseThrow(() -> new OficinaNaoEncontradaException(idOficina));
		TipoServico tipo = tipoServicoRepository.findById(dto.getIdTipoServico()).orElseThrow(() -> new TipoServicoNaoEncontradoException(dto.getIdTipoServico()));
		Veiculo veiculo = veiculoRepository.findById(dto.getIdVeiculo()).orElseThrow(() -> new VeiculoNaoEncontradoException(dto.getIdVeiculo()));
		StatusServico status = statusServicoRepository.findById(dto.getIdStatusServico()).orElseThrow(() -> new StatusServicoNaoEncontradoException(dto.getIdStatusServico()));
		Servico servico = new Servico();
		servico.setReceberNotificacao(dto.isReceberNotificacao());
		servico.setTokenAtualizacao(UUID.randomUUID().toString().replace("-",""));
		servico.setTokenConsulta(TokenUtil.gerarCodigo(6));
		
		servico.setOficina(oficina);
		servico.setTipoServico(tipo);
		servico.setVeiculo(veiculo);
		servico.setStatusServico(status);
		if(status.getDescricao().equals("INICIADO")) {
			EtapaServico etapa = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemAsc(tipo).orElseThrow(() -> new EtapaServicoNaoEncontradaException("Nenhuma etapa encontrada para o serviço: " + tipo.getDescricao()));
			servico.setEtapaServico(etapa);
			servico.setDataInicio(new Timestamp(System.currentTimeMillis()));
		}
		Servico salvo = servicoRepository.save(servico);
		Map<String, Object> detalhes = AuditoriaFormatter.formatarServico(salvo);
		auditoriaService.registrar(
				Acao.CREATE,
				Entidade.SERVICO,
				salvo.getId(),
				detalhes);
		return new ServicoResDTO(salvo);
	}
	
	public ServicoResDTO atualizar(ServicoReqDTO dto, Long idServico) {
		Servico servico = servicoRepository.findById(idServico).orElseThrow(() -> new ServicoNaoEncontradoException(idServico)); 
		StatusServico status = statusServicoRepository.findById(dto.getIdStatusServico()).orElseThrow(() -> new StatusServicoNaoEncontradoException(dto.getIdStatusServico()));
		
		Map<String, Object> antes = AuditoriaFormatter.formatarServico(servico);
		
		servico.setReceberNotificacao(dto.isReceberNotificacao());
		
		//Se mudou status
		if(servico.getStatusServico().getId() != status.getId()) {
			servico.setStatusServico(status);
			TipoServico tipo = servico.getTipoServico();
			if(status.getDescricao().equals("INICIADO")) {
				//Seleciona a primeira etapa
				EtapaServico etapa = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemAsc(tipo).orElseThrow(() -> new EtapaServicoNaoEncontradaException("Nenhuma etapa encontrada para o serviço: " + tipo.getDescricao()));
				servico.setDataInicio(new Timestamp(System.currentTimeMillis()));
				servico.setDataFim(null);
				servico.setEtapaServico(etapa);
				//TODO notifica cliente
			}else if(status.getDescricao().equals("FINALIZADO")){
				servico.setDataFim(new Timestamp(System.currentTimeMillis()));
				servico.setEtapaServico(null);
			}else {
				//Remove etapa caso mude para "AGENDADO"
				servico.setDataFim(null);
				servico.setDataInicio(null);
				servico.setEtapaServico(null);				
			}			
		}
		
		Servico atualizado = servicoRepository.save(servico);
		Map<String, Object> depois = AuditoriaFormatter.formatarServico(atualizado);
		Map<String, Object> alteracoes = new LinkedHashMap<>();
		alteracoes.put("antes", antes);
		alteracoes.put("depois", depois);
		
		auditoriaService.registrar(
				Acao.UPDATE,
				Entidade.SERVICO,
				idServico,
				alteracoes);
		
		return new ServicoResDTO(atualizado);
	}
	
	public void deletar(Long idServico) {
		Servico servico = servicoRepository.findById(idServico).orElseThrow(() -> new ServicoNaoEncontradoException(idServico));
		servicoRepository.delete(servico);
		Map<String, Object> detalhes = AuditoriaFormatter.formatarServico(servico);
		auditoriaService.registrar(
				Acao.DELETE,
				Entidade.SERVICO,
				servico.getId(),
				detalhes);
	}
	
	public byte[] gerarQrCode(Long idServico) {
		Servico s = servicoRepository.findById(idServico).orElseThrow(() -> new ServicoNaoEncontradoException());
		
		String url = baseUrl + "/servicos/atualizar-etapa?token=" + s.getTokenAtualizacao();
		try {
			return QrCodeUtil.gerarQrCode(url);
		} catch (WriterException | IOException e) {
			e.printStackTrace();
			throw new ErroAoGerarQrCode(e.getMessage());
		}
	}

	public void atualizarEtapa(String token) {
		Servico servico = servicoRepository.findByTokenAtualizacao(token).orElseThrow(() -> new ServicoNaoEncontradoException(token));
		
		if(!servico.getOficina().getAtivo()){
			throw new OficinaDesativadaException(servico.getOficina().getNome());
		}
		
		if(!servico.getStatusServico().getDescricao().equals("INICIADO")) {
			return;
		}
		
		Map<String, Object> antes = AuditoriaFormatter.formatarServico(servico);		
		EtapaServico etapaAtual = servico.getEtapaServico();
		EtapaServico ultima = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemDesc(servico.getTipoServico())
				.orElseThrow(() -> new EtapaServicoNaoEncontradaException("Nenhuma etapa cadastrada"));
		
		if(etapaAtual.getId() == ultima.getId()) {
			servico.setDataFim(new Timestamp(System.currentTimeMillis()));
			servico.setStatusServico(statusServicoRepository.findByDescricao("FINALIZADO").orElseThrow(() -> new StatusServicoNaoEncontradoException("FINALIZADO")));
			servico.setEtapaServico(null);
		}else {			
			EtapaServico etapaNova = etapaServicoRepository.findFirstByTipoServicoAndOrdemGreaterThanOrderByOrdemAsc(
					servico.getTipoServico(),
					etapaAtual.getOrdem()
					).orElseThrow(() -> new OrdemEtapaNaoEncontradoException(etapaAtual.getOrdem(), etapaAtual.getTipoServico().getDescricao()));
			servico.setEtapaServico(etapaNova);
		}
		Servico atualizado = servicoRepository.save(servico);
		
		Map<String, Object> depois = AuditoriaFormatter.formatarServico(atualizado);
		Map<String, Object> alteracoes = new LinkedHashMap<>();
		alteracoes.put("antes", antes);
		alteracoes.put("depois", depois);
		
		auditoriaService.registrar(
				Acao.UPDATE,
				Entidade.SERVICO,
				servico.getId(),
				alteracoes);
		
		historicoEtapaService.criar(atualizado, etapaAtual);
		
		if(servico.getReceberNotificacao()) {
			//TODO notificar cliente
		}
	}

	public Map<String, Object> validarTokenAtualizacao(String token) {
		Servico servico = servicoRepository.findByTokenAtualizacao(token).orElseThrow(() -> new ServicoNaoEncontradoException(token));
		if(!servico.getStatusServico().getDescricao().equals("INICIADO")) {
			throw new EtapaServicoNaoEncontradaException("Servico não iniciado ou finalizado");
		}
		
		Map<String, Object> dto = new HashMap<>();
		dto.put("servico", new CadastroResDTO(servico));
		
		
		EtapaServico etapaAtual = servico.getEtapaServico();
		EtapaServico ultima = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemDesc(servico.getTipoServico())
				.orElseThrow(() -> new EtapaServicoNaoEncontradaException("Nenhuma etapa cadastrada"));	
		if(etapaAtual.getId().equals(ultima.getId())) {
			dto.put("proximaEtapa", "FINALIZADO");
			return dto;
		}
		
		EtapaServico etapaNova = etapaServicoRepository.findFirstByTipoServicoAndOrdemGreaterThanOrderByOrdemAsc(
				servico.getTipoServico(),
				etapaAtual.getOrdem()
		).orElseThrow(() -> new OrdemEtapaNaoEncontradoException(etapaAtual.getOrdem(), etapaAtual.getTipoServico().getDescricao()));
		dto.put("proximaEtapa", new EtapaServicoResDTO(etapaNova));
		return dto;
	}
}
