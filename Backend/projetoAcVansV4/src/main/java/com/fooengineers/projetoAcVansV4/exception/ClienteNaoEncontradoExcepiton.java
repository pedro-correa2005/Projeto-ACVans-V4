package com.fooengineers.projetoAcVansV4.exception;

public class ClienteNaoEncontradoExcepiton extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ClienteNaoEncontradoExcepiton(Long id) {
		super("Cliente com o id " + id + " não encontrado.");
	}
}
