package com.fooengineers.projetoAcVansV4.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.MediaEtapasDTO;
import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.HistoricoEtapa;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.repository.EtapaServicoRepository;
import com.fooengineers.projetoAcVansV4.repository.HistoricoEtapaRepository;
import com.fooengineers.projetoAcVansV4.specification.HistoricoEtapaSpecification;

@Service
public class HistoricoEtapaService {
	@Autowired
	private HistoricoEtapaRepository historicoEtapaRepository;
	@Autowired
	private EtapaServicoRepository etapaServicoRepository;
	
	public List<MediaEtapasDTO> calcularMediaEtapas(int ano, int mes, Oficina oficina){
		YearMonth ym = YearMonth.of(ano, mes);
		LocalDateTime inicio = ym.atDay(1).atStartOfDay();
		LocalDateTime fim = ym.atEndOfMonth().atTime(LocalTime.MAX);
		
		List <HistoricoEtapa> historicoEtapa = historicoEtapaRepository.findAll(HistoricoEtapaSpecification.filtroMesAno(inicio, fim, oficina.getId()));
		
		List<MediaEtapasDTO> mediaEtapas = new ArrayList<MediaEtapasDTO>();
		
		for(EtapaServico etapa: etapaServicoRepository.findByOficina(oficina, Sort.by("ordem"))) {
			int soma = 0;
			float media = 0;
			List <HistoricoEtapa> filtrado = historicoEtapa.stream().filter(e -> e.getEtapaServico().getId() == etapa.getId()).toList();
			for(HistoricoEtapa historico : filtrado) {
				soma += historico.getTempoMinutos();
			}
			media = ((float) soma) / (filtrado.size());
			mediaEtapas.add(new MediaEtapasDTO(etapa.getTipoServico().getDescricao(), etapa.getTitulo(), media));
		}
		return mediaEtapas;
	}
}
