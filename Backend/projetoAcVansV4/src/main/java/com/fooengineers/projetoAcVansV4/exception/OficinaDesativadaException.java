package com.fooengineers.projetoAcVansV4.exception;

public class OficinaDesativadaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OficinaDesativadaException(String oficina) {
		super("Oficina " + oficina + " desativada. Entre em contato com um gerente ou administrador.");
	}

}
