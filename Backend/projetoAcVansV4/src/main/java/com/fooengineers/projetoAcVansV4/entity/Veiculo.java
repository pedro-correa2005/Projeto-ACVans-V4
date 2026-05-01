package com.fooengineers.projetoAcVansV4.entity;

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
public class Veiculo {
	@Id
	@Column(name = "id_veiculo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String placa;
	
	@Column(nullable = false)
	private String marca;
	
	@Column(nullable = false)
	private String modelo;
	
	@ManyToOne
	@JoinColumn(name = "fk_cliente", nullable = false)
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name = "fk_oficina", nullable = false)
	private Oficina oficina;
}
