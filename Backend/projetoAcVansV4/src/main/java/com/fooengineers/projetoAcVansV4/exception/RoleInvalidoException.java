package com.fooengineers.projetoAcVansV4.exception;

public class RoleInvalidoException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RoleInvalidoException(String nome) {
		super("Perfil de usuário inválido: " + nome);
	}
}
