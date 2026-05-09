package com.fooengineers.projetoAcVansV4.exception;

public class TipoServicoNaoEncontradoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TipoServicoNaoEncontradoException(Long id) {
		super("TIpo de serviço com o id " + id + " não foi encontrado");
	}
}
