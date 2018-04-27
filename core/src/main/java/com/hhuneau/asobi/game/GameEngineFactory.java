package com.hhuneau.asobi.game;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameEngineFactory {

    private final List<GameEngine> gameEngines;

    public GameEngineFactory(List<GameEngine> gameEngines) {
        this.gameEngines = gameEngines;
    }

    public GameEngine getEngine(GameType gameType) {
        return gameEngines.stream()
            .filter(ge -> ge.isInterested(gameType))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }
}
