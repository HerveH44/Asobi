package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.booster.Slot;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SlotDTO {
    public int occurrences;
    public List<SlotType> values;

    public static SlotDTO of(Slot slot) {
        final SlotDTO slotDTO = new SlotDTO();
        slotDTO.occurrences = 1;
        slotDTO.values = slot.getValues();
        return slotDTO;
    }
}
