package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameMode;

import java.util.List;

public interface PoolMaker {

    List<Booster> createPools(Game game);

    boolean isInterested(GameMode gameMode);
}
