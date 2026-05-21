package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.entity.Veiculo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoResDTO {
	private Long id;
	private String placa;
	private String marca;
	private String modelo;
	private ClienteResDTO cliente;
	
	//Constructor a partir de entity.Veiculo
	public VeiculoResDTO(Veiculo veiculo) {
		this.id = veiculo.getId();
		this.placa = veiculo.getPlaca();
		this.marca = veiculo.getMarca();
		this.modelo = veiculo.getModelo();
		this.cliente = new ClienteResDTO(veiculo.getCliente());
	}
}
