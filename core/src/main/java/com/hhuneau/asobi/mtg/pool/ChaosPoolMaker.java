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
    private static final Date modernDate = new GregorianCalendar(2003, 7, 27).getTime();

    public ChaosPoolMaker(MTGSetsService setsService, List<SetBoosterMaker> setBoosterMakers) {
        this.setsService = setsService;
        this.setBoosterMakers = setBoosterMakers;
    }

    @Override
    public List<Booster> createPools(Game game) {

        final List<MTGSet> setsPool = setsService.getSets()
            .stream()
            .filter(set -> set.getType().matches("expansion|core"))
            .filter(set -> !game.isModernOnly() || set.getReleaseDate().after(modernDate))
            .collect(Collectors.toList());

        final List<Booster> boosters = new ArrayList<>();

        game.getPlayers().forEach(player -> {
            Collections.shuffle(setsPool);
            final List<Booster> playerBoosters =
                !game.isTotalChaos()
                    ? getPlayerBoosters(game, setsPool, player)
                    : getTotalChaosPlayerBoosters(game.getPacksNumber(), setsPool, player);
            boosters.addAll(playerBoosters);
            player.setPool(playerBoosters);
        });

        return boosters;
    }

    private List<Booster> getTotalChaosPlayerBoosters(int packsNumber, List<MTGSet> setsPool, Player player) {
        final List<Booster> totalChaosBoosters = new ArrayList<>();
        for (int i = 0; i < packsNumber; i++) {
            Collections.shuffle(setsPool);
            final List<MTGCard> cards = new ArrayList<>();

            //Add one rare
            Collections.shuffle(setsPool);
            final CardsGroupedByRarity cardsGroupedByRarity = CardsGroupedByRarity.of(setsPool.get(0));
            if (!cardsGroupedByRarity.get(MYTHIC_RARE).isEmpty() && new Random().nextInt(8) == 0) {
                cards.add(getRandomCard(MYTHIC_RARE, setsPool.get(0)));
            } else {
                Collections.shuffle(setsPool);
                while (CardsGroupedByRarity.of(setsPool.get(0)).get(RARE).isEmpty()) {
                    Collections.shuffle(setsPool);
                }
                cards.add(getRandomCard(RARE, setsPool.get(0)));
            }

            //Add 3 uncommon
            for (int j = 0; j < 3; j++) {
                Collections.shuffle(setsPool);
                while (CardsGroupedByRarity.of(setsPool.get(0)).get(UNCOMMON).isEmpty()) {
                    Collections.shuffle(setsPool);
                }
                cards.add(getRandomCard(Rarity.UNCOMMON, setsPool.get(0)));
            }

            //Add 10 common
            for (int j = 0; j < 10; j++) {
                Collections.shuffle(setsPool);
                while (CardsGroupedByRarity.of(setsPool.get(0)).get(COMMON).isEmpty()) {
                    Collections.shuffle(setsPool);
                }
                cards.add(getRandomCard(Rarity.COMMON, setsPool.get(0)));
            }

            totalChaosBoosters.add(Booster.of(player, null, cards));
        }
        return totalChaosBoosters;
    }

    private MTGCard getRandomCard(Rarity rarity, MTGSet mtgSet) {
        final List<MTGCard> mtgCardList = CardsGroupedByRarity.of(mtgSet).get(rarity);
        Collections.shuffle(mtgCardList);
        return mtgCardList.get(0);
    }

    private List<Booster> getPlayerBoosters(Game game, List<MTGSet> setsPool, Player player) {
        return setsPool.subList(0, game.getPacksNumber()).stream()
            .map(set -> {
                final List<MTGCard> cards = setBoosterMakers.stream()
                    .filter(bMaker -> bMaker.isInterestedIn(set.getCode()))
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
