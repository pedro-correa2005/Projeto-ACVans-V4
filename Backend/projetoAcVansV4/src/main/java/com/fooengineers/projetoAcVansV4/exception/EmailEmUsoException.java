package com.fooengineers.projetoAcVansV4.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class EmailEmUsoException extends DataIntegrityViolationException {

	public EmailEmUsoException(String email) {
		super("Usuario com email " + email + " já existente.");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
