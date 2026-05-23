package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.entity.Servico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicoResDTO {
	private Long id;
	private String tokenConsulta;
	private boolean receberNotificacao;
	private String dataInicio;
	private String dataFIm;
	private TipoServicoResDTO tipoServico;
	private StatusServicoDTO statusServico;
	private EtapaServicoResDTO etapaServico;
	
	//Constructor a partir de entity.Servico
	public ServicoResDTO(Servico servico) {
		this.id = servico.getId();
		this.tokenConsulta = servico.getTokenConsulta();
		this.receberNotificacao = servico.getReceberNotificacao();
		this.dataInicio = servico.getDataInicio().toString();
		this.dataFIm = servico.getDataFim() != null? servico.getDataFim().toString():null;
		this.tipoServico = new TipoServicoResDTO(servico.getTipoServico());
		this.statusServico = new StatusServicoDTO(servico.getStatusServico());
		this.etapaServico = servico.getEtapaServico() != null ? new EtapaServicoResDTO(servico.getEtapaServico()): null;
	}
}
