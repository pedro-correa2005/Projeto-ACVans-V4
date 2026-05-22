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
	private String receberNotificacao;
	private String dataInicio;
	private String dataFIm;
	private String tipoServico;
	private String statusServico;
	private String etapaServico;
	
	//Constructor a partir de entity.Servico
	public ServicoResDTO(Servico servico) {
		this.id = servico.getId();
		this.tokenConsulta = servico.getTokenConsulta();
		this.receberNotificacao = servico.getReceberNotificacao()?"Sim":"Não";
		this.dataInicio = servico.getDataInicio().toString();
		this.dataFIm = servico.getDataFim() != null? servico.getDataFim().toString():null;
		this.tipoServico = servico.getTipoServico().getDescricao();
		this.statusServico = servico.getStatusServico().getDescricao();
		this.etapaServico = servico.getEtapaServico() != null ? servico.getEtapaServico().getTitulo():null;
	}
}
