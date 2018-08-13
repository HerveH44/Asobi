package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.LAND;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.COMMON;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.RARE;

@Component
public class DGMBoosterMaker extends DefaultBoosterMaker {

    private final MTGSetsService setsService;
    private MTGSet rtr;
    private List<MTGCard> shockLands = new ArrayList<>();

    public DGMBoosterMaker(MTGSetsService setsService) {
        this.setsService = setsService;
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("DGM");
    }

    @Override
    List<MTGCard> handleUnexpectedSlotValue(MTGSet set, SlotType slotType, int occurrences) {
        final CardsGroupedByRarity groupedByRarity = CardsGroupedByRarity.of(set);
        if (slotType.equals(LAND)) {
            if (new Random().nextInt(20) == 0) {

                if (rtr == null) {
                    rtr = setsService.getSet("RTR").orElseThrow(() -> new IllegalStateException("KTK set not found"));
                    shockLands.addAll(
                        CardsGroupedByRarity.of(rtr).get(RARE).stream()
                            .filter(card -> card.getType().equals("Land") && !card.getName().equals("Grove of the Guardian"))
                            .collect(Collectors.toList()));
                }
                return choose(shockLands, occurrences);
            } else {
                return choose(groupedByRarity.get(COMMON).stream()
                    .filter(card -> card.getType().equals("Land"))
                    .collect(Collectors.toList()), occurrences);
            }
        }
        return null;
    }

}
