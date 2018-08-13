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
public class FRFBoosterMaker extends DefaultBoosterMaker {

    private final MTGSetsService setsService;
    private MTGSet ktk;
    private List<MTGCard> fetchLands = new ArrayList<>();

    public FRFBoosterMaker(MTGSetsService setsService) {
        this.setsService = setsService;
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("FRF");
    }

    @Override
    List<MTGCard> handleUnexpectedSlotValue(MTGSet set, SlotType slotType, int occurrences) {
        final CardsGroupedByRarity groupedByRarity = CardsGroupedByRarity.of(set);
        if (slotType.equals(LAND)) {
            if (new Random().nextInt(20) == 0) {

                if (ktk == null) {
                    ktk = setsService.getSet("KTK").orElseThrow(() -> new IllegalStateException("KTK set not found"));
                    fetchLands.addAll(
                        CardsGroupedByRarity.of(ktk).get(RARE).stream()
                            .filter(card -> card.getType().equals("Land"))
                            .collect(Collectors.toList()));
                }
                return choose(fetchLands, occurrences);
            } else {
                return choose(groupedByRarity.get(COMMON).stream()
                    .filter(card -> card.getType().equals("Land"))
                    .collect(Collectors.toList()), occurrences);
            }
        }
        return null;
    }
}
