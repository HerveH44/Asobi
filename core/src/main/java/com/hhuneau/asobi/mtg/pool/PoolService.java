package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;

public interface PoolService {

    void createPools(Game game);

    void delete(Booster booster);
}
