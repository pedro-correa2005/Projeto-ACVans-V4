package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class smtpEmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void enviar2FACode(String email, String code) throws MessagingException, IOException {
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(email);
		helper.setSubject("Seu código de verificação");
		helper.setText(load2FATemplate(code), true);
		
		mailSender.send(message);
	}
	
	public String load2FATemplate(String code) throws IOException{
		InputStream is = getClass()
				.getClassLoader()
				.getResourceAsStream("templates/2fa-template.html");
		String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		
		return html.replace("{{CODE}}", code);
	}
}
