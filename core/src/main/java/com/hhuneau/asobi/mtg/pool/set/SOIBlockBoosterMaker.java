package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.card.Rarity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.*;

@Component
public class SOIBlockBoosterMaker extends DefaultBoosterMaker {

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.matches("SOI|EMN");
    }

    @Override
    protected List<MTGCard> handleUnexpectedSlotValues(MTGSet set, List<SlotType> slotValues, int occurrences) {

        final CardsGroupedByRarity cardsGroupedByRarity = CardsGroupedByRarity.of(set);
        final List<MTGCard> cards = new ArrayList<>();

        for (int i = 1; i < occurrences + 1; i++) {
            if (slotValues.size() == 2) {
                final SlotType slotType = slotValues.get(new Random().nextInt(2));
                cards.addAll(handleSlotType(set, cardsGroupedByRarity, slotType, 1));
            }

            if (slotValues.size() == 3 &&
                slotValues.containsAll(List.of(COMMON, DOUBLE_FACED_MYTHIC_RARE, DOUBLE_FACED_RARE))) {
                if (new Random().nextInt(8) != 0) {
                    cards.addAll(choose(cardsGroupedByRarity.get(Rarity.COMMON), 1));
                } else if (new Random().nextInt(15) < 3) {
                    cards.addAll(handleSlotType(set, cardsGroupedByRarity, DOUBLE_FACED_MYTHIC_RARE, 1));
                } else {
                    cards.addAll(handleSlotType(set, cardsGroupedByRarity, DOUBLE_FACED_RARE, 1));
                }
            }
        }

        return cards;
    }
}
