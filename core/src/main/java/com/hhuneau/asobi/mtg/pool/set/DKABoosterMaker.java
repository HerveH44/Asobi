package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.booster.Slot;

import java.util.List;
import java.util.Random;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.*;

public class DKABoosterMaker extends DefaultBoosterMaker {
    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("DKA");
    }

    @Override
    List<Slot> additionalProcessSlotValues(MTGSet set, List<Slot> filteredList) {
        //http://www.mtgsalvation.com/forums/magic-fundamentals/magic-general/327956-innistrad-block-transforming-card-pack-odds?comment=4
        //80 card sheet, 2 mythic, 6 rare (8), 24 uncommon (32), 48 common
        final int randomNumber = new Random().nextInt(80);
        for (int i = 0; i < filteredList.size(); i++) {
            final Slot slot = filteredList.get(i);
            if (isCommonSlot(slot)) {
                if (randomNumber == 0) {
                    slot.setValues(List.of(DOUBLE_FACED_MYTHIC_RARE));
                } else if (randomNumber < 8) {
                    slot.setValues(List.of(DOUBLE_FACED_RARE));
                } else if (randomNumber < 32) {
                    slot.setValues(List.of(DOUBLE_FACED_UNCOMMON));
                } else {
                    slot.setValues(List.of(DOUBLE_FACED_COMMON));
                }
                filteredList.set(i, slot);
                return filteredList;
            }
        }

        return filteredList;
    }

    private boolean isCommonSlot(Slot slot) {
        return slot.getValues().size() == 1 && slot.getValues().contains(COMMON);
    }
}
