package com.fooengineers.projetoAcVansV4.auditoria.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Detalhes {
	private Map<String, Object> dados;
}
