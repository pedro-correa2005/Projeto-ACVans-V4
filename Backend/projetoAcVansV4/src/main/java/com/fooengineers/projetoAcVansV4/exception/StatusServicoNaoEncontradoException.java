package com.fooengineers.projetoAcVansV4.exception;

public class StatusServicoNaoEncontradoException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StatusServicoNaoEncontradoException(Integer idStatus) {
		super("Não foi possível encontrar status de serviço com id " + idStatus);
	}
}
