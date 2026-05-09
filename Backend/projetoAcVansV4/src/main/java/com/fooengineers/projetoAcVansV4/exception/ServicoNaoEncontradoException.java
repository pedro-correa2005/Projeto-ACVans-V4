package com.fooengineers.projetoAcVansV4.exception;

public class ServicoNaoEncontradoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ServicoNaoEncontradoException(Long id) {
		super("Não foi encontrado Serviço com o id: " + id);
	}
	public ServicoNaoEncontradoException(String token) {
		super("Não foi encontrado Serviço com o token: " + token);
	}
	public ServicoNaoEncontradoException() {
		super("Serviço não encontrado");
	}
}
