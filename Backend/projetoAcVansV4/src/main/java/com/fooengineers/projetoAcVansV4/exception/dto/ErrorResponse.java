package com.fooengineers.projetoAcVansV4.exception.dto;
import java.time.LocalDateTime;

import lombok.Data;

//Classe para padronizar todas as respostas de erro
@Data
public class ErrorResponse {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
	
	public ErrorResponse(int status, String error, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message= message;
		this.path = path;
	}
}
