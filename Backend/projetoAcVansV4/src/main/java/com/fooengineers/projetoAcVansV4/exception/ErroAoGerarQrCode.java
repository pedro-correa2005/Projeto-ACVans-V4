package com.fooengineers.projetoAcVansV4.exception;

public class ErroAoGerarQrCode extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErroAoGerarQrCode(String message) {
		super("Erro ao gerar qr code: " + message);
	}

}
