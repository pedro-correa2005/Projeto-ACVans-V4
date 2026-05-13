package com.fooengineers.projetoAcVansV4.exception;

public class EtapaServicoNaoEncontradaException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EtapaServicoNaoEncontradaException(Long id) {
		super("Etapa de serviço com id " + id + " não econtrada");
	}
	public EtapaServicoNaoEncontradaException(String tipoServico, int ordem) {
		super("Etapa de serviço com ordem " + ordem + " não econtrada para o tipo de serviço: " + tipoServico);
	}
	public EtapaServicoNaoEncontradaException(String message) {
		super(message);
	}	
}
