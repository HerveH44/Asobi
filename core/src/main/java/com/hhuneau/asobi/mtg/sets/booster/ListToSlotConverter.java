package com.hhuneau.asobi.mtg.sets.booster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class ListToSlotConverter implements AttributeConverter<List<SlotType>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<SlotType> data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SlotType> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, TypeFactory.defaultInstance().constructCollectionType(List.class, SlotType.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
