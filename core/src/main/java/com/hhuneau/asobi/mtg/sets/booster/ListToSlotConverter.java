package com.hhuneau.asobi.mtg.sets.booster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class ListToSlotConverter implements AttributeConverter<List<Slot>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Slot> data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Slot> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data,
                TypeFactory.defaultInstance().constructCollectionType(List.class, Slot.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
