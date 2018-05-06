package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameMode;

import java.util.List;

public interface PoolMaker {

    List<Booster> createPools(Game game);

    boolean isInterested(GameMode gameMode);
}
