package com.fooengineers.projetoAcVansV4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etapa_servico")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtapaServico {
	@Id
	@Column(name = "id_etapa_servico")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Integer ordem;
	
	@Column(nullable = false)
	private String titulo;
	
	@Column(nullable = false)
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name = "fk_tipo_servico", nullable = false)
	private TipoServico tipoServico;
	
	@ManyToOne
	@JoinColumn(name = "fk_oficina", nullable = false)
	private Oficina oficina;
}
