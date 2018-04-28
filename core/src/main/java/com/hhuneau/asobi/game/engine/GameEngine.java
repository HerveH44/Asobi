package com.hhuneau.asobi.game.engine;

import com.hhuneau.asobi.game.GameType;

public interface GameEngine {

    void start(Long gameId);
    boolean isInterested(GameType gameType);
}
