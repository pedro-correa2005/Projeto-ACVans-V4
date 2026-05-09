package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.entity.Servico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicoResDTO {
	private String tokenConsulta;
	private String receberNotificacao;
	private String dataInicio;
	private String dataFIm;
	
	//Constructor a partir de entity.Servico
	public ServicoResDTO(Servico servico) {
		this.tokenConsulta = servico.getTokenConsulta();
		this.receberNotificacao = servico.getReceberNotificacao()?"Sim":"Não";
		this.dataInicio = servico.getDataInicio().toString();
		this.dataFIm = servico.getDataFim().toString();
	}
}
