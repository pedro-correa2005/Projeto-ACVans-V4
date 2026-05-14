package com.fooengineers.projetoAcVansV4.domain;

public enum Entidade {
	CLIENTE("Cliente"), 
	VEICULO("Veículo"), 
	TIPO_SERVICO("Tipo de Serviço"),
	ETAPA_SERVICO("Etapa de Serviço"),
	SERVICO("Servico");
	
	private String descricao;
	Entidade(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
