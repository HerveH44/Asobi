package com.hhuneau.asobi.game;

public interface GameEngine {

    void start(Long gameId);
    boolean isInterested(GameType gameType);
}
