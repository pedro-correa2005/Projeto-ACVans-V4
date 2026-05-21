package com.fooengineers.projetoAcVansV4.exception;

public class ReordenacaoNaoPermitidaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReordenacaoNaoPermitidaException() {
		super("Não foi possível reordenar. Há serviços desse tipo em andamento.");
	}

}
