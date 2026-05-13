package com.fooengineers.projetoAcVansV4.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Servico {
	@Id
	@Column(name = "id_servico")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "receber_notificacao")
	private Boolean receberNotificacao = true;
	
	@Column(name = "data_inicio", nullable = false)
	private Timestamp dataInicio;
	
	@Column(name = "data_fim")
	private Timestamp dataFim;
	
	@Column(name = "token_atualizacao", unique = true, nullable = false)
	private String tokenAtualizacao;
	
	@Column(name = "token_consulta", unique = true, nullable = false)
	private String tokenConsulta;
	
	@ManyToOne
	@JoinColumn(name = "fk_veiculo", nullable = false)
	private Veiculo veiculo;
	
	@ManyToOne
	@JoinColumn(name = "fk_tipo_servico", nullable = false)
	private TipoServico tipoServico;
	
	@ManyToOne
	@JoinColumn(name = "fk_status_servico", nullable = false)
	private StatusServico statusServico;
	
	@ManyToOne
	@JoinColumn(name = "fk_etapa_servico", nullable = true)
	private EtapaServico etapaServico;
	
	@ManyToOne
	@JoinColumn(name = "fk_oficina", nullable = false)
	private Oficina oficina;
	
}
