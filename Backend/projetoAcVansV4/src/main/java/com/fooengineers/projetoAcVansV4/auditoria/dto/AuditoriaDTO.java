package com.fooengineers.projetoAcVansV4.auditoria.dto;

import com.fooengineers.projetoAcVansV4.auditoria.entity.Auditoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaDTO {
	private Long id;
	private String acao;
	private String entidade;
	private Long idRegistro;
	private String tempo;
	private String enderecoIp;
	private Detalhes detalhes;
	private String usuario;
	
	//Constructor a partir de entity.Auditoria
	public AuditoriaDTO(Auditoria auditoria) {
		this.id = auditoria.getId();
		
		this.acao = auditoria.getAcao().getDescricao();
		
		this.entidade = auditoria.getEntidade() != null? auditoria.getEntidade().getDescricao() : null;
		
		this.idRegistro = auditoria.getIdRegistro();
		
		this.tempo = auditoria.getTempo().toString();
		
		this.enderecoIp = auditoria.getEnderecoIp();
		
		if(auditoria.getDetalhes() != null)
			this.detalhes = auditoria.getDetalhes();
		
		if(auditoria.getUsuario() != null)
			this.usuario = auditoria.getUsuario().getEmail();
	}
}
