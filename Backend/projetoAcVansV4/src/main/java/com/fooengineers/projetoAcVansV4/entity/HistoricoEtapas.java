package com.fooengineers.projetoAcVansV4.entity;

import java.sql.Timestamp;

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
@Table(name = "historico_etapa_servico")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoEtapas {
	@Id
	@Column(name = "id_historico")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "data_inicio")
	private Timestamp dataInicio;
	
	@Column(name = "data_fim")
	private Timestamp dataFim;
	
	@Column(name = "tempo_minutos")
	private Integer tempoMinutos;
	
	@ManyToOne
	@JoinColumn(name = "fk_servico", nullable = false)
	private Servico servico;
	
	@ManyToOne
	@JoinColumn(name = "fk_etapa_servico", nullable = false)
	private EtapaServico etapaServico;
	
	@ManyToOne
	@JoinColumn(name = "fk_oficina", nullable = false)
	private Oficina oficina;
}
