package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameMode;
import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.card.Rarity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hhuneau.asobi.mtg.game.GameMode.NORMAL;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.*;

@Component
// TODO: Merge with PoolService
public class NormalPoolMaker implements PoolMaker {
    private static final int UNCOMMON_CARDS = 3;
    private static final int RARE_CARD = 1;
    private static final int COMMON_CARDS = 10;

    public List<Booster> createPools(Game game) {
        final List<MTGSet> sets = game.getSets();
        final List<Booster> boosters = new ArrayList<>();
        game.getPlayers().forEach(player -> {
            final List<Booster> playerBoosters = sets.stream()
                .map(set ->
                    Booster.of(player, set, makeCardPool(set)))
                .collect(Collectors.toList());
            boosters.addAll(playerBoosters);
            player.setPool(playerBoosters);
        });
        return boosters;
    }

    @Override
    public boolean isInterested(GameMode gameMode) {
        return gameMode.equals(NORMAL);
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
