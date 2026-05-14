package com.fooengineers.projetoAcVansV4.auditoria.entity;

import java.sql.Timestamp;

import com.fooengineers.projetoAcVansV4.auditoria.converter.DetalhesConverter;
import com.fooengineers.projetoAcVansV4.auditoria.dto.Detalhes;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auditoria {
	@Id
	@Column(name = "id_auditoria")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Acao acao;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Entidade entidade;
	
	@Column(name = "id_registro")
	private Long idRegistro;
	
	@Column(nullable = false)
	private Timestamp tempo;
	
	@Column(name = "enderecoIp")
	private String enderecoIp;
	
	@Convert(converter = DetalhesConverter.class)
	@Column(columnDefinition = "json")
	private Detalhes detalhes;
	
	@ManyToOne
	@JoinColumn(name = "fk_usuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "fk_oficina")
	private Oficina oficina;
}
