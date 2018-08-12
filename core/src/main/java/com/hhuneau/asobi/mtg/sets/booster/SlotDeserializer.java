package com.hhuneau.asobi.mtg.sets.booster;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.*;

public class SlotDeserializer extends StdDeserializer<Slot> {

    public SlotDeserializer() {
        this(null);
    }

    private SlotDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    @Transactional
    public Slot deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        final Slot slot = new Slot();
        slot.setValues(new ArrayList<>());

        if (node instanceof TextNode) {
            addValue(slot, node);
        }

        if (node instanceof ArrayNode) {
            node.elements().forEachRemaining(child -> addValue(slot, child));
        }

        return slot;
    }

    private void addValue(Slot slot, JsonNode node) {
        switch (node.asText()) {
            case "checklist":
                slot.getValues().add(CHECKLIST);
                break;
            case "common":
                slot.getValues().add(COMMON);
                break;
            case "double faced":
                slot.getValues().add(DOUBLE_FACED);
                break;
            case "double faced mythic rare":
                slot.getValues().add(DOUBLE_FACED_MYTHIC_RARE);
                break;
            case "timeshifted uncommon":
                slot.getValues().add(TIMESHIFTED_UNCOMMON);
                break;
            case "double faced common":
                slot.getValues().add(DOUBLE_FACED_COMMON);
                break;
            case "double faced rare":
                slot.getValues().add(DOUBLE_FACED_RARE);
                break;
            case "land":
                slot.getValues().add(LAND);
                break;
            case "double faced uncommon":
                slot.getValues().add(DOUBLE_FACED_UNCOMMON);
                break;
            case "marketing":
                slot.getValues().add(MARKETING);
                break;
            case "rare":
                slot.getValues().add(RARE);
                break;
            case "mythic rare":
                slot.getValues().add(MYTHIC_RARE);
                break;
            case "timeshifted common":
                slot.getValues().add(TIMESHIFTED_COMMON);
                break;
            case "timeshifted purple":
                slot.getValues().add(TIMESHIFTED_PURPLE);
                break;
            case "timeshifted rare":
                slot.getValues().add(TIMESHIFTED_RARE);
                break;
            case "uncommon":
                slot.getValues().add(UNCOMMON);
                break;
        }
    }
}
