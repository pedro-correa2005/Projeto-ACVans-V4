package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class smtpEmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void enviar2FACode(String email, String code) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(email);
		message.setSubject("Seu código de verificação");
		message.setText("Seu código 2FA é: " + code + "\n"
				+ "Expira em 5 minutos.");
		
		mailSender.send();
	}
	
	public String load2FATemplate(String code) throws IOException{
		InputStream is = getClass()
				.getClassLoader()
				.getResourceAsStream("templates/2fa-template.html");
		String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		
		return html.replace("{{CODE}}", code);
	}
}
