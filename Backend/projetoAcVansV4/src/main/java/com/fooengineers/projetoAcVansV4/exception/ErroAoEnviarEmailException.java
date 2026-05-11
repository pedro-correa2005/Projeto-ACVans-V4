package com.fooengineers.projetoAcVansV4.exception;

public class ErroAoEnviarEmailException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErroAoEnviarEmailException(String message, Throwable cause) {
		super(message, cause);
	}	
}
