package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.LAND;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.BASIC_LAND;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.COMMON;

@Component
public class M19BoosterMaker extends DefaultBoosterMaker {

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("M19");
    }

    @Override
    List<MTGCard> handleUnexpectedSlotValue(MTGSet set, SlotType slotType, int occurrences) {
        final CardsGroupedByRarity groupedByRarity = CardsGroupedByRarity.of(set);
        if (slotType.equals(LAND)) {
            if(new Random().nextInt(12) < 5) {
                final List<MTGCard> dualLands = groupedByRarity.get(COMMON).stream()
                    .filter(card ->card.getType().equals("Land"))
                    .collect(Collectors.toList());

                return choose(dualLands, occurrences);
            } else {
                return choose(groupedByRarity.get(BASIC_LAND), occurrences);
            }
        }
        return null;
    }

    @Override
    List<MTGCard> handleCommon(CardsGroupedByRarity cardsByRarity, int occurrences) {
        final List<MTGCard> commonsWithoutDualLand = cardsByRarity.get(COMMON).stream()
            .filter(card -> !card.getType().equals("Land"))
            .collect(Collectors.toList());
        return choose(commonsWithoutDualLand, occurrences);
    }
}
