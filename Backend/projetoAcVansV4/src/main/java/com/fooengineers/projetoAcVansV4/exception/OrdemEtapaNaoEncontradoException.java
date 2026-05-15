package com.fooengineers.projetoAcVansV4.exception;

public class OrdemEtapaNaoEncontradoException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrdemEtapaNaoEncontradoException(int ordem, String tipo) {
		super("Não encontrado etapa de ordem " + ordem + " para o tipo de servico " + tipo + "/n"
				+ "Verifique que o serviço não está na última etapa, e que há etapas cadastradas");
	}
}
