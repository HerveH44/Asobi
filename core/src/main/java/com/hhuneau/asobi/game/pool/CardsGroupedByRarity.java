package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.sets.MTGCard;
import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.game.sets.Rarity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CardsGroupedByRarity {
    private Map<Rarity, List<MTGCard>> map;

    public static CardsGroupedByRarity of(MTGSet set) {
        final CardsGroupedByRarity instance = new CardsGroupedByRarity();
        instance.map = set.getCards().stream()
            .collect(Collectors.groupingBy(MTGCard::getRarity));
        return instance;
    }

    public List<MTGCard> get(Rarity rarity) {
        return map.getOrDefault(rarity, Collections.emptyList());
    }
}
