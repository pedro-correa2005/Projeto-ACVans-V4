package com.fooengineers.projetoAcVansV4.exception;


public class UsuarioNaoEncontradoException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioNaoEncontradoException(Long idUsuario) {
		super("Usuário com id " + idUsuario + " não encontrado");
	}
	
}
