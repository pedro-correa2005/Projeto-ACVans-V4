package com.fooengineers.projetoAcVansV4.exception;

public class VeiculoNaoEncontradoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VeiculoNaoEncontradoException(Long id) {
		super("Não foi encontrado veículo com id: " + id);
	}
	public VeiculoNaoEncontradoException(String placa) {
		super("Não foi encontrado veículo com placa: " + placa);
	}
}
