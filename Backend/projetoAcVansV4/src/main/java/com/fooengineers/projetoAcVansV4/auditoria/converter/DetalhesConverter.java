package com.fooengineers.projetoAcVansV4.auditoria.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooengineers.projetoAcVansV4.auditoria.dto.Detalhes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DetalhesConverter implements AttributeConverter<Detalhes, String>{
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String convertToDatabaseColumn(Detalhes obj) {
		try {
			return mapper.writeValueAsString(obj);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Detalhes convertToEntityAttribute(String json) {
		try {
			return mapper.readValue(json, Detalhes.class);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
