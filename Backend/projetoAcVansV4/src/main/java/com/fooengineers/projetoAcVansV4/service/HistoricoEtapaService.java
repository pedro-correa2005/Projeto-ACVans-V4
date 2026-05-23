package com.fooengineers.projetoAcVansV4.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.MediaEtapasDTO;
import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.HistoricoEtapa;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.exception.EtapaServicoNaoEncontradaException;
import com.fooengineers.projetoAcVansV4.exception.OficinaDesativadaException;
import com.fooengineers.projetoAcVansV4.repository.EtapaServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.HistoricoEtapaRepository;
import com.fooengineers.projetoAcVansV4.repository.TipoServicoRepository;
import com.fooengineers.projetoAcVansV4.specification.HistoricoEtapaSpecification;

@Service
public class HistoricoEtapaService {
	@Autowired
	private HistoricoEtapaRepository historicoEtapaRepository;
	@Autowired
	private EtapaServicoRepository etapaServicoRepository;
	@Autowired
	private TipoServicoRepository tipoServicoRepository;
	
	public void criar(Servico servico, EtapaServico anterior) {
		if(anterior.equals(null)) return;
		if(!servico.getOficina().getAtivo()) {
			throw new OficinaDesativadaException(servico.getOficina().getNome());
		}
		
		HistoricoEtapa historico = new HistoricoEtapa();
		EtapaServico primeiraEtapa = etapaServicoRepository.findFirstByTipoServicoOrderByOrdemAsc(servico.getTipoServico()).orElseThrow(() -> new EtapaServicoNaoEncontradaException("Nenhuma etapa cadastrada para o tipo de servico: " + servico.getTipoServico().getDescricao()));
	
		historico.setServico(servico);
		historico.setEtapaServico(anterior);
		
		Timestamp inicio = null;
		Timestamp fim = Timestamp.valueOf(LocalDateTime.now());
		
		if(anterior.getOrdem().equals(primeiraEtapa.getOrdem())) {
			inicio = servico.getDataInicio();
		}else {
			EtapaServico antAnterior = etapaServicoRepository.findFirstByTipoServicoAndOrdemLessThanOrderByOrdemDesc(servico.getTipoServico(), anterior.getOrdem()).orElseThrow(() -> new EtapaServicoNaoEncontradaException("Não encontrada etapa anterior"));
			inicio = this.buscarPorServicoAndEtapa(servico, antAnterior).getDataFim();
		}
		
		historico.setDataInicio(inicio);
		historico.setDataFim(fim);
		historico.setTempoMinutos((int) Duration.between(inicio.toInstant(), fim.toInstant()).toMinutes());
		historico.setOficina(servico.getOficina());
		historicoEtapaRepository.save(historico);
	}
	
	private HistoricoEtapa buscarPorServicoAndEtapa(Servico servico, EtapaServico etapa) {
		HistoricoEtapa hs = historicoEtapaRepository.findByServicoAndEtapaServico(servico, etapa).orElse(null);
		
		return hs;
	}

	public List<MediaEtapasDTO> calcularMediaEtapas(int ano, int mes, Oficina oficina){
		YearMonth ym = YearMonth.of(ano, mes);
		LocalDateTime inicio = ym.atDay(1).atStartOfDay();
		LocalDateTime fim = ym.atEndOfMonth().atTime(LocalTime.MAX);
		
		List <HistoricoEtapa> historicoEtapa = historicoEtapaRepository.findAll(HistoricoEtapaSpecification.filtroMesAno(inicio, fim, oficina.getId()));
		
		List<MediaEtapasDTO> listaMediaEtapas = new ArrayList<MediaEtapasDTO>();
		
		for(TipoServico tipo: tipoServicoRepository.findByOficina(oficina)) {
			List<MediaEtapasDTO.Etapa> etapasDTO = new ArrayList<MediaEtapasDTO.Etapa>();			
			for(EtapaServico etapa: etapaServicoRepository.findByTipoServico(tipo)) {
				List <HistoricoEtapa> filtrado = historicoEtapa.stream().filter(e -> e.getEtapaServico().getId().equals(etapa.getId())).toList();
				
				if(filtrado.isEmpty()) continue;
				
				int soma = 0;
				for(HistoricoEtapa historico : filtrado) {
					soma += historico.getTempoMinutos();
				}
				float media = ((float) soma) / (filtrado.size());
				etapasDTO.add(new MediaEtapasDTO.Etapa(etapa.getTitulo(), media));
			}
			MediaEtapasDTO dto = new MediaEtapasDTO();
			dto.setTipoServicoDescricao(tipo.getDescricao());
			dto.setEtapas(etapasDTO);
			listaMediaEtapas.add(dto);
		}
		return listaMediaEtapas;
	}
}
