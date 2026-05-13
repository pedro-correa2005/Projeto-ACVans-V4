package com.fooengineers.projetoAcVansV4.util;

import java.security.SecureRandom;

public class TokenUtil {
	public static String gerarCodigo(int tamanho) {
		String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
		SecureRandom random = new SecureRandom();
		
		StringBuilder stringBuilder = new StringBuilder(tamanho);
		for(int i = 0; i < tamanho; i++) {
			stringBuilder.append(chars.charAt(random.nextInt(chars.length())));
		}
		return stringBuilder.toString();
	}
}
