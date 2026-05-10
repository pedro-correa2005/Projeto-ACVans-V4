package com.fooengineers.projetoAcVansV4.util;

import java.security.SecureRandom;

public class SenhaUtil {
	private static final String CARACTERES =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
					+ "abcdefghijklmnopqrstuvwxyz"
					+ "0123456789"
					+ "!@#$%";
	
	private static final SecureRandom random = new SecureRandom();
	
	public static String gerarSenha(int tamanho) {
		StringBuilder senha= new StringBuilder();
		for(int i = 0; i < tamanho; i++) {
			senha.append(CARACTERES.charAt(random.nextInt(CARACTERES.length())));
		}
		return senha.toString();
	}
}
