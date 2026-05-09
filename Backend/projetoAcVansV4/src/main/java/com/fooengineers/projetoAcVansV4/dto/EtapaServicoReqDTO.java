package com.fooengineers.projetoAcVansV4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtapaServicoReqDTO {
	@NotBlank(message="Titulo é obrigatorio")
	String titulo;
	@NotBlank(message="Descrição obrigatória")
	String descricao;
}
