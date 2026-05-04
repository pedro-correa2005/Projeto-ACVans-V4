package com.fooengineers.projetoAcVansV4.security;

import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.service.RedisService;

@Service
public class MFAService {
	@Autowired
	private RedisService redisService;
	@Autowired
	private static SecureRandom random;
	
	//Gera código aleatório de 6 dígitos
	public String gerarCodigo() {
		return String.valueOf(100000 + random.nextInt(900000));
	}

	//Salva códgio no redis com duração de 5 minutos
	public String salvarCodigo(String email, String code) {
		//Salva "sessão" no redis
		String tempToken = UUID.randomUUID().toString();
		redisService.save2FASession(tempToken, email, 300);
		//Salva o código no redis
		redisService.save2FACode(email, code, 300);
		//Salva número de tentativas
		redisService.save2FAAttempts(tempToken, 300);
		return tempToken;
	}
	
	//Envia email
	public void enviarEmail(String email, String code) {
		System.out.println("Seu código é " + code);
	}
	
	//Verifica código
	public boolean verificarCodigo(String email, String code) {
		//Pega o código armazenado em redis
		String storedCode = redisService.get(email);
		//Verifica se o código existe
		if(storedCode == null) {
			return false;
		}
		//Verifica se o código enviado é igual ao recebido
		if(!storedCode.equals(code)) {
			return false;
		}
		//Código válido: Apaga código
		redisService.delete(email);
		return true;
	}
	
	//Incrementa e Verifica número de tentativas
	public boolean verificarAttempts(String tempToken) {
		if(redisService.increment2FAAttempts(tempToken) > 5) {
			//Apaga token
			delete2FA(redisService.get(tempToken) ,tempToken);
			//Retorna falso
			return false;
		}
		return true;
	}
	
	//Verifica tempToken: devolve email se for válido, null se for inválido
	public String verificarTempToken(String tempToken) {
		return redisService.get(tempToken); 
	}
	
	//Deleta tudo
	public void delete2FA(String email, String tempToken) {
		redisService.delete(email);
		redisService.delete(tempToken);
		redisService.delete("2fa_attempts:" + tempToken);
	}
}
