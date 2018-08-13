package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.*;

@Component
public class MastersBoosterMaker extends DefaultBoosterMaker {

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.matches("MMA|MM2|MM3|IMA|EMA|M25");
    }

    @Override
    protected List<MTGCard> handleUnexpectedSlotValues(MTGSet set, List<SlotType> slotValues, int occurrences) {
        if (slotValues.size() == 4 &&
            slotValues.containsAll(List.of(FOIL_MYTHIC_RARE, FOIL_RARE, FOIL_COMMON, FOIL_UNCOMMON))) {
            return handleFoil(set, occurrences);
        }
        return null;
    }
}
