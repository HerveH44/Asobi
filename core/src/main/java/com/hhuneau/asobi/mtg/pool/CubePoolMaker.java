package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameMode;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.GameMode.CUBE;

@Component
public class CubePoolMaker implements PoolMaker {

    @Override
    public List<Booster> createPools(Game game) {
        List<MTGCard> cubeList = game.getCubeList();
        Collections.shuffle(cubeList);
        final List<Booster> boosters = new ArrayList<>();
        for (int i = 0; i < game.getPlayers().size(); i++) {
            final Player player = game.getPlayers().get(i);
            final List<Booster> playerBoosters = new ArrayList<>();
            for (int j = 0; j < game.getPacksNumber(); j++) {
                final List<MTGCard> boosterCards = cubeList.stream()
                    .skip(15 * (j + (game.getPacksNumber() * i)))
                    .limit(15)
                    .collect(Collectors.toList());
                playerBoosters.add(Booster.of(player, null, boosterCards));
            }
            boosters.addAll(playerBoosters);
            player.setPool(playerBoosters);
        }
        return boosters;
    }

    @Override
    public boolean isInterested(GameMode gameMode) {
        return gameMode.equals(CUBE);
    }
}
