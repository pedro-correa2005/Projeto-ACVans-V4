package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import com.fooengineers.projetoAcVansV4.exception.OficinaNaoEncontradaException;
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
	@Value("${app.base-url}")
	private String baseUrl;

	public Page<ServicoResDTO> listar(Oficina oficina, Pageable pageable) {
		return servicoRepository.findByOficina(oficina, pageable).map(ServicoResDTO::new);
	}
	
	public List<ServicoResDTO> buscarPorVeiculo(Long idVeiculo, String termo){
		return servicoRepository.findAll(ServicoSpecification.filtrarPorVeiculo(termo, idVeiculo))
				.stream().map(ServicoResDTO::new).collect(Collectors.toList());
	}
	
	public ServicoResDTO criar(ServicoReqDTO dto, Integer idOficina) {
		Oficina oficina = oficinaRepository.findById(idOficina).orElseThrow(() -> new OficinaNaoEncontradaException(idOficina));
		TipoServico tipo = tipoServicoRepository.findById(dto.getIdTipoServico()).orElseThrow(() -> new TipoServicoNaoEncontradoException(dto.getIdTipoServico()));
		Veiculo veiculo = veiculoRepository.findById(dto.getIdVeiculo()).orElseThrow(() -> new VeiculoNaoEncontradoException(dto.getIdVeiculo()));
		StatusServico status = statusServicoRepository.findById(dto.getIdStatusServico()).orElseThrow(() -> new StatusServicoNaoEncontradoException(dto.getIdStatusServico()));
		Servico servico = new Servico();
		servico.setReceberNotificacao(dto.isReceberNotificacao());
		servico.setDataInicio(new Timestamp(System.currentTimeMillis()));
		servico.setDataFim(dto.getDataFim());
		servico.setTokenAtualizacao(UUID.randomUUID().toString().replace("-",""));
		servico.setTokenConsulta(TokenUtil.gerarCodigo(6));
		
		servico.setOficina(oficina);
		servico.setTipoServico(tipo);
		servico.setVeiculo(veiculo);
		servico.setStatusServico(status);
		if(status.getDescricao().equals("INICIADO")) {
			EtapaServico etapa = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemAsc(tipo).orElseThrow(() -> new EtapaServicoNaoEncontradaException("Nenhuma etapa encontrada para o serviço: " + tipo.getDescricao()));
			servico.setEtapaServico(etapa);
		}
		
		return new ServicoResDTO(servicoRepository.save(servico));
	}
	
	public ServicoResDTO atualizar(ServicoReqDTO dto, Long idServico) {
		Servico servico = servicoRepository.findById(idServico).orElseThrow(() -> new ServicoNaoEncontradoException(idServico)); 
		StatusServico status = statusServicoRepository.findById(dto.getIdStatusServico()).orElseThrow(() -> new StatusServicoNaoEncontradoException(dto.getIdStatusServico()));
		
		servico.setReceberNotificacao(dto.isReceberNotificacao());
		servico.setDataFim(dto.getDataFim());
		servico.setStatusServico(status);
		
		if(status.getDescricao().equals("INICIADO")) {
			TipoServico tipo = servico.getTipoServico();
			EtapaServico etapa = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemAsc(tipo).orElseThrow(() -> new EtapaServicoNaoEncontradaException("Nenhuma etapa encontrada para o serviço: " + tipo.getDescricao()));
			servico.setEtapaServico(etapa);
		}
		
		return new ServicoResDTO(servicoRepository.save(servico));
	}
	
	public void deletar(Long idServico) {
		Servico servico = servicoRepository.findById(idServico).orElseThrow(() -> new ServicoNaoEncontradoException(idServico));
		servicoRepository.delete(servico);
	}
	
	public byte[] gerarQrCode(Long idServico) {
		Servico s = servicoRepository.findById(idServico).orElseThrow(() -> new ServicoNaoEncontradoException());
		
		String url = baseUrl + "/servicos/atualizar-status?token=" + s.getTokenAtualizacao();
		try {
			return QrCodeUtil.gerarQrCode(url);
		} catch (WriterException | IOException e) {
			e.printStackTrace();
			throw new ErroAoGerarQrCode(e.getMessage());
		}
	}
}
