package com.fooengineers.projetoAcVansV4.security;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.service.RedisService;
import com.fooengineers.projetoAcVansV4.service.smtpEmailService;

import jakarta.mail.MessagingException;

@Service
public class MFAService {
	@Autowired
	private RedisService redisService;
	@Autowired
	private smtpEmailService smtpEmailService;
	
	//Gera código aleatório de 6 dígitos
	public String gerarCodigo() {
		SecureRandom random = new SecureRandom();
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
		try {
			smtpEmailService.enviar2FACode(email, code);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			deletar2FA(redisService.get(tempToken) ,tempToken);
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
	public void deletar2FA(String email, String tempToken) {
		redisService.delete(email);
		redisService.delete(tempToken);
		redisService.delete("2fa_attempts:" + tempToken);
	}
}
