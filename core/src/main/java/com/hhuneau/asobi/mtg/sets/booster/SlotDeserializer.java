package com.hhuneau.asobi.mtg.sets.booster;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.*;

public class SlotDeserializer extends StdDeserializer<List<Slot>> {

    public SlotDeserializer() {
        this(null);
    }

    private SlotDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    @Transactional
    public List<Slot> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        final List<Slot> slotList = new ArrayList<>();

        if (node instanceof ArrayNode) {
            node.elements().forEachRemaining(child -> {
                final Slot slot = new Slot();
                if (child instanceof ArrayNode) {
                    child.elements().forEachRemaining(slotType -> addValue(slot, slotType));
                } else {
                    addValue(slot, child);
                }
                slotList.add(slot);
            });
        }

        return slotList;
    }

    private void addValue(Slot slot, JsonNode node) {
        switch (node.asText()) {
            case "checklist":
                slot.add(CHECKLIST);
                break;
            case "common":
                slot.add(COMMON);
                break;
            case "double faced":
                slot.add(DOUBLE_FACED);
                break;
            case "double faced mythic rare":
                slot.add(DOUBLE_FACED_MYTHIC_RARE);
                break;
            case "timeshifted uncommon":
                slot.add(TIMESHIFTED_UNCOMMON);
                break;
            case "double faced common":
                slot.add(DOUBLE_FACED_COMMON);
                break;
            case "double faced rare":
                slot.add(DOUBLE_FACED_RARE);
                break;
            case "land":
                slot.add(LAND);
                break;
            case "double faced uncommon":
                slot.add(DOUBLE_FACED_UNCOMMON);
                break;
            case "marketing":
                slot.add(MARKETING);
                break;
            case "rare":
                slot.add(RARE);
                break;
            case "mythic rare":
                slot.add(MYTHIC_RARE);
                break;
            case "timeshifted common":
                slot.add(TIMESHIFTED_COMMON);
                break;
            case "timeshifted purple":
                slot.add(TIMESHIFTED_PURPLE);
                break;
            case "timeshifted rare":
                slot.add(TIMESHIFTED_RARE);
                break;
            case "foil mythic rare":
                slot.add(FOIL_MYTHIC_RARE);
                break;
            case "foil rare":
                slot.add(FOIL_RARE);
                break;
            case "foil uncommon":
                slot.add(FOIL_UNCOMMON);
                break;
            case "foil common":
                slot.add(FOIL_COMMON);
                break;
            case "uncommon":
                slot.add(UNCOMMON);
                break;
        }
    }
}
