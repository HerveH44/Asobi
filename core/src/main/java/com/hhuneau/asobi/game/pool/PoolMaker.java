package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.Player;
import com.hhuneau.asobi.game.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.game.sets.MTGCard;
import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.game.sets.Rarity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hhuneau.asobi.game.sets.Rarity.*;

@Component
public class PoolMaker {
    private static final int UNCOMMON_CARDS = 3;
    private static final int RARE_CARD = 1;
    private static final int COMMON_CARDS = 10;

    public List<Pool> createPools(Set<Player> players, List<MTGSet> sets) {
        final List<Pool> pools = new ArrayList<>();
        players.forEach(player -> pools.addAll(
            sets.stream()
                .map(set ->
                    Pool.of(player, set, makeCardPool(set)))
                .collect(Collectors.toList())));
        return pools;
    }

    private List<MTGCard> makeCardPool(MTGSet set) {
        final CardsGroupedByRarity cardsByRarity = CardsGroupedByRarity.of(set);

        return Stream.of(
            chooseRare(cardsByRarity),
            chooseUnco(cardsByRarity),
            chooseCommon(cardsByRarity)
        ).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private List<MTGCard> chooseCommon(CardsGroupedByRarity cardsByRarity) {
        return choose(COMMON_CARDS, cardsByRarity.get(COMMON));
    }

    private List<MTGCard> chooseRare(CardsGroupedByRarity cardsByRarity) {
        final Rarity rarity = hasMythics(cardsByRarity) && isMythicCard() ? MYTHIC_RARE : RARE;
        return choose(RARE_CARD, cardsByRarity.get(rarity));
    }

    private boolean hasMythics(CardsGroupedByRarity cardsByRarity) {
        return !cardsByRarity.get(MYTHIC_RARE).isEmpty();
    }

    private boolean isMythicCard() {
        return new Random().nextInt(8) == 0;
    }

    private List<MTGCard> chooseUnco(CardsGroupedByRarity cardsByRarity) {
        return choose(UNCOMMON_CARDS, cardsByRarity.get(UNCOMMON));
    }

    private List<MTGCard> choose(int number, List<MTGCard> list) {
        return shuffle(list).subList(0, Math.min(number, list.size()));
    }

    private List<MTGCard> shuffle(List<MTGCard> list) {
        Collections.shuffle(list);
        return list;
    }
}
