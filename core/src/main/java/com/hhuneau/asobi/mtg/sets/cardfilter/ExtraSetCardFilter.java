package com.hhuneau.asobi.mtg.sets.cardfilter;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.entry;

@Component
public class ExtraSetCardFilter implements MTGCardFilter {

    private static final Map<String, Integer> map = Map.ofEntries(
        entry("M15", 270),
        entry("ORI", 273),
        entry("KLD", 264),
        entry("AER", 184),
        entry("AKH", 269),
        entry("HOU", 199),
        entry("XLN", 279),
        entry("RIX", 196),
        entry("DOM", 269),
        entry("M19", 280)
    );

    @Override
    public boolean isInterested(MTGSet set) {
        return map.containsKey(set.getCode());
    }

    @Override
    public Set<MTGCard> apply(MTGSet set, Set<MTGCard> cards) {
        return cards.stream()
            .filter(mtgCard -> {
                final Integer cardNumber = Integer.parseInt(mtgCard.getNumber().replaceAll("\\D", ""));
                final Integer maxNumber = map.get(set.getCode());
                return maxNumber >= cardNumber;
            })
            .collect(Collectors.toSet());
    }
}
