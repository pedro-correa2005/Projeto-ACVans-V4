package com.fooengineers.projetoAcVansV4.auditoria.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooengineers.projetoAcVansV4.auditoria.dto.Detalhes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DetalhesConverter implements AttributeConverter<Detalhes, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Detalhes attribute) {
        if (attribute == null) return null;

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter Detalhes para JSON", e);
        }
    }

    @Override
    public Detalhes convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            return objectMapper.readValue(dbData, Detalhes.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter JSON para Detalhes", e);
        }
    }
}