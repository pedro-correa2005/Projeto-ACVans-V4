package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
		sendTemplateEmail(email, "Seu código de verificação", "2fa-template.html", Map.of("CODE", code));
	}
	
	public void enviarResetSenha(String email, String token) throws MessagingException, IOException  {
		String link = urlSite + "/auth/reset-senha?token=" + token;
		sendTemplateEmail(email, "Redefinição de senha", "reset-senha-template.html", Map.of("LINK", link));
	}
	
	public void enviarSenhaInicial(String email, String senha) throws MessagingException, IOException {
		sendTemplateEmail(email, "Conta criada", "senha-inicial-template.html", Map.of("EMAIL", email, "SENHA", senha));
	}
	
	private void sendTemplateEmail(String to, String subject, String templateName, Map<String, String> variables) throws MessagingException, IOException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(loadTemplate(templateName, variables));
		
		mailSender.send(message);
	}
	
	private String loadTemplate(String templateName, Map<String, String> variables) throws IOException{
		InputStream is = getClass()
				.getClassLoader()
				.getResourceAsStream("templates/" + templateName);
		
		if(is == null) {
			throw new IOException("Template não encontrado: " + templateName);
		}
		
		String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		
		for (Map.Entry<String, String> entry: variables.entrySet()) {
			html = html.replace("{{" + entry.getKey() + "}}", entry.getValue());
		}
		
		return html;
	}
}
