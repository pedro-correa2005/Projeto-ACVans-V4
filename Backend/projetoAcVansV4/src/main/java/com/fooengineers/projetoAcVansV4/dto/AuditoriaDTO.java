package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Auditoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaDTO {
	private String acao;
	private String entidade;
	private Long idRegistro;
	private String tempo;
	private String enderecoIp;
	private String detalhes;
	private String email;
	
	//Constructor a partir de entity.Auditoria
	public AuditoriaDTO(Auditoria auditoria) {
		this.acao = auditoria.getAcao().getDescricao();
		
		this.entidade = auditoria.getEntidade() != null? auditoria.getEntidade().getDescricao() : null;
		
		this.idRegistro = auditoria.getIdRegistro();
		
		this.tempo = auditoria.getTempo().toString();
		
		this.enderecoIp = auditoria.getEnderecoIp();
		
		if(auditoria.getDetalhes() != null)
			this.detalhes = auditoria.getDetalhes().getAntes().toString() + auditoria.getDetalhes().getDepois().toString();
		
		if(auditoria.getUsuario() != null)
			this.email = auditoria.getUsuario().getEmail();
	}
}
