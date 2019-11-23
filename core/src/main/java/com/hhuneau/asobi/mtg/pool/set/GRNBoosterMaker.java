package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.LAND;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.COMMON;

@Component
public class GRNBoosterMaker extends DefaultBoosterMaker {
    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("GRN");
    }

    @Override
    protected List<MTGCard> handleUnexpectedSlotValue(MTGSet set, SlotType slotType, int occurrences) {
        final CardsGroupedByRarity groupedByRarity = CardsGroupedByRarity.of(set);
        if (slotType.equals(LAND)) {
            final List<MTGCard> dualLands = groupedByRarity.get(COMMON).stream()
                .filter(card -> card.getType().equals("Land"))
                .collect(Collectors.toList());

            return choose(dualLands, occurrences);
        }
        return null;
    }

    @Override
    protected List<MTGCard> handleCommon(CardsGroupedByRarity cardsByRarity, int occurrences) {
        final List<MTGCard> commonsWithoutDualLand = cardsByRarity.get(COMMON).stream()
            .filter(card -> !card.getType().equals("Land"))
            .collect(Collectors.toList());
        return choose(commonsWithoutDualLand, occurrences);
    }
}
