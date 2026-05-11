package com.fooengineers.projetoAcVansV4.exception;

public class OficinaNaoEncontradaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public OficinaNaoEncontradaException(Integer id) {
		super("Não foi encontrada oficina com o id: " + id);
	}
}
