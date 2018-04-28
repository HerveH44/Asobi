package com.hhuneau.asobi.game.engine;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameType;

public interface GameEngine {

    void start(Game game);

    boolean isInterested(GameType gameType);
}
