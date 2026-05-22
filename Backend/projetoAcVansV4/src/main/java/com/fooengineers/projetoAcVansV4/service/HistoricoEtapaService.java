package com.fooengineers.projetoAcVansV4.service;

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
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
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
