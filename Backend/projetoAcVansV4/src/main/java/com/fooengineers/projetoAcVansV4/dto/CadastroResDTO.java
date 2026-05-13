package com.fooengineers.projetoAcVansV4.dto;

import java.sql.Timestamp;

import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.entity.Veiculo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroResDTO {
	private long idServico;
	private String tokenConsulta;
	private String veiculo;
	private String descricaoTipoServico;
	private String nomeCliente;
	private String celularCliente;
	private String receberNotificacao;
	private Timestamp dataInicio;
	private String etapaTitulo;
	private Timestamp dataFim;
	
	//Constructor a partir de entity.Servico
	public CadastroResDTO(Servico servico) {
		this.idServico = servico.getId();
		this.tokenConsulta = servico.getTokenConsulta();
		Veiculo veiculo = servico.getVeiculo();
		this.veiculo = veiculo.getPlaca() + " " + veiculo.getMarca() + " " + veiculo.getModelo();
		TipoServico tipo = servico.getTipoServico();
		this.descricaoTipoServico = tipo.getDescricao();
		Cliente cliente = veiculo.getCliente();
		this.nomeCliente = cliente.getNome();
		this.celularCliente = cliente.getCelular();
		this.receberNotificacao = servico.getReceberNotificacao()?"Sim":"Não";
		this.dataInicio = servico.getDataInicio();
		EtapaServico etapa = servico.getEtapaServico();
		this.etapaTitulo = etapa != null? etapa.getTitulo():null;
		this.dataFim = servico.getDataFim();
	}
}
