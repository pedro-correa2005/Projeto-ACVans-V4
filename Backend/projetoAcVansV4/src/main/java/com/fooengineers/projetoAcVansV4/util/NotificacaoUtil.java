package com.fooengineers.projetoAcVansV4.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Servico;

@Component
public class NotificacaoUtil {
	@Value("${app.base-url}")
	private String baseUrl;
	@Value("${z-api.clientToken}")
	private String clientToken;
	@Value("${z-api.instanceId}")
	private String instanceId;
	@Value("${z-api.instanceToken}")
	private String instanceToken;
	
	public void notificarCliente(Servico servico, String mensagem) {
		Cliente cliente = servico.getVeiculo().getCliente();
		String nome = cliente.getNome().split(" ")[0];
		String celular = cliente.getCelular();
		
		String jsonBody = String.format(
				"""
				{
					"phone": "%s",
					"message":"%s"
				}
				"""
				, "55" + celular.replace("(", "").replace(")", "").replaceAll("\\s", ""), formularMensagem(nome, servico.getTokenConsulta(), servico.getVeiculo().getPlaca(), mensagem)
		);
		System.out.println(jsonBody);
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.z-api.io/instances/" + instanceId + "/token/" + instanceToken + "/send-text"))
				.header("Content-Type", "application/json")
				.header("Client-Token", clientToken)
				.POST(BodyPublishers.ofString(jsonBody))
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println("Código de resposta: " + response.statusCode());
            System.out.println("Corpo da resposta: " + response.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String formularMensagem(String nome, String token, String placa, String m) {
		String mensagem;
		if(LocalTime.now().getHour() >= 18 || LocalTime.now().getHour() < 6) {
			mensagem = "Boa Noite, " + nome + "!\n";
		}else if(LocalTime.now().getHour() >= 12) {
			mensagem = "Boa Tarde, " + nome + "!\n";
		}else {
			mensagem = "Bom Dia, " + nome + "!\n";
		}
		
		mensagem = mensagem.concat(m + "\n"
				+ "Confira mais informações em: "+ baseUrl + "/consulta?token=" + token + "&placa=" + placa
		);
		System.out.println(mensagem);
		return mensagem;
	}
}
