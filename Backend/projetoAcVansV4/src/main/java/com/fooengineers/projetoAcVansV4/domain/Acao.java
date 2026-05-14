package com.fooengineers.projetoAcVansV4.domain;

public enum Acao {
	CREATE("Cadastro"),
	READ("Consulta"), 
	UPDATE("Atualização"), 
	DELETE("Exclusão"),
	LOGIN_FAIL("Tentativa de Login falha"),
	LOGIN("Efetuou login");
	
	private String descricao;
	Acao(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
