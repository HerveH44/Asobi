package com.hhuneau.asobi.game;

import org.springframework.stereotype.Component;

import static com.hhuneau.asobi.game.GameType.DRAFT;

@Component
public class DraftEngine implements GameEngine {

    @Override
    public void start(Long gameId) {

    }

    @Override
    public boolean isInterested(GameType gameType) {
        return gameType == DRAFT;
    }
}
