package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameMode;
import com.hhuneau.asobi.mtg.pool.set.DefaultBoosterMaker;
import com.hhuneau.asobi.mtg.pool.set.SetBoosterMaker;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.GameMode.NORMAL;

@Component
// TODO: Merge with PoolService
public class NormalPoolMaker implements PoolMaker {
    private final List<SetBoosterMaker> setBoosterMakers;

    public NormalPoolMaker(List<SetBoosterMaker> setBoosterMakers) {
        this.setBoosterMakers = setBoosterMakers;
    }

    public List<Booster> createPools(Game game) {
        final List<MTGSet> sets = game.getSets();
        final List<Booster> boosters = new ArrayList<>();
        game.getPlayers().forEach(player -> {
            final List<Booster> playerBoosters = sets.stream()
                .map(set -> {
                    final List<MTGCard> cards = setBoosterMakers.stream()
                        .filter(bMaker -> bMaker.isInterestedIn(set.getCode()))
                        .findFirst()
                        .orElse(new DefaultBoosterMaker())
                        .make(set);
                    return Booster.of(player, set, cards);
                })
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
}
