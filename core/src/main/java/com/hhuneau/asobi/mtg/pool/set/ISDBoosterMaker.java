package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.booster.Slot;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.*;

@Component
public class ISDBoosterMaker extends DefaultBoosterMaker {

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("ISD");
    }

    @Override
    protected List<Slot> additionalProcessSlotValues(MTGSet set, List<Slot> filteredList) {
        //http://www.mtgsalvation.com/forums/magic-fundamentals/magic-general/327956-innistrad-block-transforming-card-pack-odds?comment=4
        //121 card sheet, 1 mythic, 12 rare (13), 42 uncommon (55), 66 common
        final int randomNumber = new Random().nextInt(121);
        for (int i = 0; i < filteredList.size(); i++) {
            final Slot slot = filteredList.get(i);
            if (isCommonSlot(slot)) {
                if (randomNumber == 0) {
                    slot.addAll(List.of(DOUBLE_FACED_MYTHIC_RARE));
                } else if (randomNumber < 13) {
                    slot.addAll(List.of(DOUBLE_FACED_RARE));
                } else if (randomNumber < 55) {
                    slot.addAll(List.of(DOUBLE_FACED_UNCOMMON));
                } else {
                    slot.addAll(List.of(DOUBLE_FACED_COMMON));
                }
                filteredList.set(i, slot);
                return filteredList;
            }
        }

        return filteredList;
    }

    private boolean isCommonSlot(Slot slot) {
        return slot.size() == 1 && slot.contains(COMMON);
    }
}
