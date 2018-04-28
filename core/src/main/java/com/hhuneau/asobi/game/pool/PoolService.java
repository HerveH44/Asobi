package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.player.Player;

import java.util.Set;

public interface PoolService {

    void createPools(Set<Player> players, Game game);
}
