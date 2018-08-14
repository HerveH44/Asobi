package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameMode;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.pool.set.DefaultBoosterMaker;
import com.hhuneau.asobi.mtg.pool.set.SetBoosterMaker;
import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.card.Rarity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.GameMode.CHAOS;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.*;

@Component
public class ChaosPoolMaker implements PoolMaker {

    private final MTGSetsService setsService;
    private final List<SetBoosterMaker> setBoosterMakers;

    public ChaosPoolMaker(MTGSetsService setsService, List<SetBoosterMaker> setBoosterMakers) {
        this.setsService = setsService;
        this.setBoosterMakers = setBoosterMakers;
    }

    @Override
    public List<Booster> createPools(Game game) {
        final List<String> setsCodes = game.isModernOnly() ? setsService.getModernSetCodes() : setsService.getTotalChaosSetCodes();

        final List<Booster> boosters = new ArrayList<>();

        game.getPlayers().forEach(player -> {
            Collections.shuffle(setsCodes);
            final List<Booster> playerBoosters =
                !game.isTotalChaos()
                    ? getPlayerBoosters(game, setsCodes, player)
                    : getTotalChaosPlayerBoosters(game.getPacksNumber(), setsCodes, player);
            boosters.addAll(playerBoosters);
            player.setPool(playerBoosters);
        });

        return boosters;
    }

    private List<Booster> getTotalChaosPlayerBoosters(int packsNumber, List<String> setsPool, Player player) {
        final List<Booster> totalChaosBoosters = new ArrayList<>();
        for (int i = 0; i < packsNumber; i++) {
            Collections.shuffle(setsPool);
            final List<MTGCard> cards = new ArrayList<>();

            //Add one rare
            MTGSet set = getRandomSet(setsPool);
            final CardsGroupedByRarity cardsGroupedByRarity = CardsGroupedByRarity.of(set);
            if (!cardsGroupedByRarity.get(MYTHIC_RARE).isEmpty() && new Random().nextInt(8) == 0) {
                cards.add(getRandomCard(MYTHIC_RARE, set));
            } else {
                set = getRandomSet(setsPool);
                while (CardsGroupedByRarity.of(set).get(RARE).isEmpty()) {
                    set = getRandomSet(setsPool);
                }
                cards.add(getRandomCard(RARE, set));
            }

            //Add 3 uncommon
            for (int j = 0; j < 3; j++) {
                set = getRandomSet(setsPool);
                while (CardsGroupedByRarity.of(set).get(UNCOMMON).isEmpty()) {
                    set = getRandomSet(setsPool);
                }
                cards.add(getRandomCard(Rarity.UNCOMMON, set));
            }

            //Add 10 common
            for (int j = 0; j < 10; j++) {
                set = getRandomSet(setsPool);
                while (CardsGroupedByRarity.of(set).get(COMMON).isEmpty()) {
                    set = getRandomSet(setsPool);
                }
                cards.add(getRandomCard(Rarity.COMMON, set));
            }

            totalChaosBoosters.add(Booster.of(player, null, cards));
        }
        return totalChaosBoosters;
    }

    private MTGSet getRandomSet(List<String> setsPool) {
        Collections.shuffle(setsPool);
        return setsService.getSet(setsPool.get(0)).get();
    }

    private MTGCard getRandomCard(Rarity rarity, MTGSet mtgSet) {
        final List<MTGCard> mtgCardList = CardsGroupedByRarity.of(mtgSet).get(rarity);
        Collections.shuffle(mtgCardList);
        return mtgCardList.get(0);
    }

    private List<Booster> getPlayerBoosters(Game game, List<String> setsPool, Player player) {
        return setsPool.subList(0, game.getPacksNumber()).stream()
            .map(setCode -> {
                final MTGSet set = setsService.getSet(setCode).get();
                final List<MTGCard> cards = setBoosterMakers.stream()
                    .filter(bMaker -> bMaker.isInterestedIn(setCode))
                    .findFirst()
                    .orElse(new DefaultBoosterMaker())
                    .make(set);
                return Booster.of(player, set, cards);
            })
            .collect(Collectors.toList());
    }

    @Override
    public boolean isInterested(GameMode gameMode) {
        return gameMode.equals(CHAOS);
    }
}
