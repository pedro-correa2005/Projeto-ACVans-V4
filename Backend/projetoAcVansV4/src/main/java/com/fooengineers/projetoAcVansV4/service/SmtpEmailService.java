package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SmtpEmailService {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	@Value("${app.base-url}")
	private String urlSite;
	
	
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
	
	public void enviarResetSenha(String email, String token) throws MessagingException, IOException  {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(email);
		helper.setSubject("Redefinição de senha");
		
		String link = urlSite + "/auth/reset-senha?token=" + token;
		
		helper.setText(loadResetSenhaTemplate(link), true);
		
		mailSender.send(message);
	}
	
	public String loadResetSenhaTemplate(String link) throws IOException{
		InputStream is = getClass()
				.getClassLoader()
				.getResourceAsStream("templates/reset-senha-template.html");
		String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		
		return html.replace("{{LINK}}", link);
	}
}
